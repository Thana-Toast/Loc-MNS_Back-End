package fr.ctanchot.locmns.unit;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.LoanDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.dao.RequestHistoryDao;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.Loan;
import fr.ctanchot.locmns.model.LoanRequest;
import fr.ctanchot.locmns.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanDao loanDao;

    @Mock
    private LoanRequestDao loanRequestDao;

    @Mock
    private RequestHistoryDao requestHistoryDao;

    @Mock
    private AppUserDao appUserDao;

    @Mock
    private fr.ctanchot.locmns.service.LoanRequestService loanRequestService;

    @InjectMocks
    private LoanService loanService;

    private Loan loan;
    private LoanRequest loanRequest;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        equipment = new Equipment();
        equipment.setId(10L);

        loanRequest = new LoanRequest();
        loanRequest.setId(4L);
        loanRequest.setEquipment(equipment);

        loan = new Loan();
        loan.setId(1L);
        loan.setStartDate(LocalDate.of(2026, 6, 1));
        loan.setEndDate(LocalDate.of(2026, 6, 15));
        loan.setLoanRequest(loanRequest);
    }

    @Test
    void save_ShouldSucceed_WhenEquipmentIsAvailable() {
        when(loanRequestDao.findById(4L)).thenReturn(Optional.of(loanRequest));

        // Start: June 1 -> Buffer check starts May 31
        // End: June 15 -> Buffer check ends June 16
        when(loanDao.existsOverlappingLoan(
                eq(10L),
                eq(1L),
                eq(LocalDate.of(2026, 5, 31)),
                eq(LocalDate.of(2026, 6, 16))
        )).thenReturn(false);

        when(loanDao.existsActiveOverdueLoan(
                eq(10L),
                eq(1L),
                any(LocalDate.class)
        )).thenReturn(false);

        when(loanDao.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan result = loanService.save(loan);

        assertNotNull(result);
        verify(loanDao, times(1)).save(loan);
        verify(requestHistoryDao, times(1)).save(any());
    }

    @Test
    void save_ShouldThrowException_WhenEquipmentIsNotAvailable() {
        when(loanRequestDao.findById(4L)).thenReturn(Optional.of(loanRequest));

        when(loanDao.existsOverlappingLoan(
                eq(10L),
                eq(1L),
                eq(LocalDate.of(2026, 5, 31)),
                eq(LocalDate.of(2026, 6, 16))
        )).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.save(loan);
        });

        assertEquals("L'équipement n'est pas disponible pour cette période (conflit de dates).", exception.getMessage());
        verify(loanDao, never()).save(any(Loan.class));
    }

    @Test
    void save_ShouldThrowException_WhenEquipmentIsOverdue() {
        when(loanRequestDao.findById(4L)).thenReturn(Optional.of(loanRequest));

        when(loanDao.existsOverlappingLoan(
                eq(10L),
                eq(1L),
                eq(LocalDate.of(2026, 5, 31)),
                eq(LocalDate.of(2026, 6, 16))
        )).thenReturn(false);

        when(loanDao.existsActiveOverdueLoan(
                eq(10L),
                eq(1L),
                any(LocalDate.class)
        )).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.save(loan);
        });

        assertEquals("L'équipement n'est pas disponible pour cette période (conflit de dates).", exception.getMessage());
        verify(loanDao, never()).save(any(Loan.class));
    }

    @Test
    void findAll_ShouldCallResolveOverdueConflicts() {
        when(loanDao.findAll()).thenReturn(java.util.List.of(loan));

        java.util.List<Loan> result = loanService.findAll();

        assertNotNull(result);
        verify(loanRequestService, times(1)).resolveOverdueConflicts();
    }
}
