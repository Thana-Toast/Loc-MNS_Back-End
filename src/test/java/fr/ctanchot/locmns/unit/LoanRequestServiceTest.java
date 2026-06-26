package fr.ctanchot.locmns.unit;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.LoanDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.dao.RequestHistoryDao;
import fr.ctanchot.locmns.model.*;
import fr.ctanchot.locmns.service.LoanRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanRequestServiceTest {

    @Mock
    private LoanRequestDao loanRequestDao;

    @Mock
    private AppUserDao appUserDao;

    @Mock
    private EquipmentDao equipmentDao;

    @Mock
    private RequestHistoryDao requestHistoryDao;

    @Mock
    private LoanDao loanDao;

    @InjectMocks
    private LoanRequestService loanRequestService;

    private AppUser user;
    private Equipment equipment;
    private Category category;
    private Model model;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setLabel("Ordinateurs Portables");
        category.setRolesWithAccess(new ArrayList<>());

        model = new Model();
        model.setId(1L);
        model.setCategory(category);

        equipment = new Equipment();
        equipment.setId(10L);
        equipment.setModel(model);

        user = new AppUser();
        user.setId(2L);
        user.setLastName("Dupont");
        user.setFirstName("Jean");

        loanRequest = new LoanRequest();
        loanRequest.setAppUser(user);
        loanRequest.setEquipment(equipment);
        loanRequest.setDescription("Besoin pro");
        loanRequest.setStartDate(LocalDate.now());
        loanRequest.setEndDate(LocalDate.now().plusDays(14));
    }

    @Test
    void save_ShouldSucceed_WhenUserHasAccessToCategory() {
        Role role = new Role();
        role.setId(1L);
        role.setLabel("ROLE_USER");
        role.setAuthorizedCategories(List.of(category));

        user.setRoles(List.of(role));
        category.setRolesWithAccess(new ArrayList<>(List.of(role)));

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findById(equipment.getId())).thenReturn(Optional.of(equipment));
        when(loanRequestDao.save(any(LoanRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanRequest result = loanRequestService.save(loanRequest, null);

        assertNotNull(result);
        assertEquals(user, result.getAppUser());
        assertEquals(equipment, result.getEquipment());
        verify(loanRequestDao, times(1)).save(loanRequest);
        verify(requestHistoryDao, times(1)).save(any(RequestHistory.class));
    }

    @Test
    void save_ShouldThrowException_WhenUserDoesNotHaveAccessToCategory() {
        Category otherCategory = new Category();
        otherCategory.setId(99L);
        otherCategory.setLabel("Casques VR");

        Role role = new Role();
        role.setId(1L);
        role.setLabel("ROLE_USER");
        role.setAuthorizedCategories(List.of(otherCategory));

        user.setRoles(List.of(role));

        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setLabel("ROLE_ADMIN");
        category.setRolesWithAccess(new ArrayList<>(List.of(adminRole)));

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findById(equipment.getId())).thenReturn(Optional.of(equipment));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanRequestService.save(loanRequest, null);
        });

        assertTrue(exception.getMessage().contains("L'utilisateur n'a pas les droits pour louer des équipements de la catégorie"));
        verify(loanRequestDao, never()).save(any(LoanRequest.class));
    }

    @Test
    void save_ShouldThrowException_WhenUserHasNoRoles() {
        user.setRoles(new ArrayList<>());

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findById(equipment.getId())).thenReturn(Optional.of(equipment));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanRequestService.save(loanRequest, null);
        });

        verify(loanRequestDao, never()).save(any(LoanRequest.class));
    }

    @Test
    void save_ShouldEnforceBufferAndOverdueCheck_WhenRequestingAvailableModelCandidate() {
        Role role = new Role();
        role.setId(1L);
        role.setLabel("ROLE_USER");
        user.setRoles(List.of(role));

        // Create a new request without equipment
        LoanRequest reqWithoutEq = new LoanRequest();
        reqWithoutEq.setAppUser(user);
        reqWithoutEq.setStartDate(LocalDate.now().plusDays(2));
        reqWithoutEq.setEndDate(LocalDate.now().plusDays(10));
        reqWithoutEq.setDescription("Besoin pro");

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findAvailableEquipmentsByModel(1L)).thenReturn(List.of(equipment));
        when(loanDao.existsActiveOverdueLoan(eq(equipment.getId()), any(), any(LocalDate.class))).thenReturn(false);
        
        // Start: now + 2 -> Buffer check starts now + 1
        // End: now + 10 -> Buffer check ends now + 11
        when(loanDao.existsOverlappingLoan(
                eq(equipment.getId()),
                any(),
                eq(LocalDate.now().plusDays(1)),
                eq(LocalDate.now().plusDays(11))
        )).thenReturn(false);

        when(loanRequestDao.save(any(LoanRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanRequest result = loanRequestService.save(reqWithoutEq, 1L);

        assertNotNull(result);
        assertEquals(equipment, result.getEquipment());
        verify(loanRequestDao, times(1)).save(reqWithoutEq);
    }

    @Test
    void resolveOverdueConflicts_ShouldReassign_WhenAnotherEquipmentIsAvailable() {
        // l1 (overdue) on equipment (id 10)
        Loan l1 = new Loan();
        l1.setId(100L);
        l1.setEndDate(LocalDate.now().minusDays(2));
        LoanRequest r1 = new LoanRequest();
        r1.setEquipment(equipment);
        l1.setLoanRequest(r1);

        // l2 (blocked conflicting loan) on equipment (id 10) starting today
        Loan l2 = new Loan();
        l2.setId(200L);
        l2.setStartDate(LocalDate.now());
        l2.setEndDate(LocalDate.now().plusDays(5));
        LoanRequest r2 = new LoanRequest();
        r2.setId(300L);
        r2.setEquipment(equipment);
        l2.setLoanRequest(r2);

        // Replacement equipment candidate (id 11)
        Equipment eq2 = new Equipment();
        eq2.setId(11L);
        eq2.setModel(model);

        when(loanDao.findOverdueLoans(any(LocalDate.class))).thenReturn(List.of(l1));
        when(loanDao.findConflictingLoans(eq(equipment.getId()), any(LocalDate.class), eq(l1.getId()))).thenReturn(List.of(l2));
        when(equipmentDao.findAvailableEquipmentsByModel(model.getId())).thenReturn(List.of(equipment, eq2));

        // eq2 candidate is not overdue
        when(loanDao.existsActiveOverdueLoan(eq(11L), any(), any(LocalDate.class))).thenReturn(false);
        // eq2 candidate does not overlap l2's period (+ 1 day buffer check: startDate - 1, endDate + 1)
        when(loanDao.existsOverlappingLoan(
                eq(11L),
                eq(l2.getId()),
                eq(l2.getStartDate().minusDays(1)),
                eq(l2.getEndDate().plusDays(1))
        )).thenReturn(false);

        loanRequestService.resolveOverdueConflicts();

        // Verify r2 has been reassigned to eq2
        assertEquals(eq2, r2.getEquipment());
        verify(loanRequestDao, times(1)).save(r2);
        verify(loanDao, never()).delete(any(Loan.class));
    }

    @Test
    void resolveOverdueConflicts_ShouldReject_WhenNoEquipmentIsAvailable() {
        // l1 (overdue) on equipment (id 10)
        Loan l1 = new Loan();
        l1.setId(100L);
        l1.setEndDate(LocalDate.now().minusDays(2));
        LoanRequest r1 = new LoanRequest();
        r1.setEquipment(equipment);
        l1.setLoanRequest(r1);

        // l2 (blocked conflicting loan) on equipment (id 10) starting today
        Loan l2 = new Loan();
        l2.setId(200L);
        l2.setStartDate(LocalDate.now());
        l2.setEndDate(LocalDate.now().plusDays(5));
        LoanRequest r2 = new LoanRequest();
        r2.setId(300L);
        r2.setEquipment(equipment);
        l2.setLoanRequest(r2);

        when(loanDao.findOverdueLoans(any(LocalDate.class))).thenReturn(List.of(l1));
        when(loanDao.findConflictingLoans(eq(equipment.getId()), any(LocalDate.class), eq(l1.getId()))).thenReturn(List.of(l2));
        
        // Only eq1 is in candidates (which is self, skipped)
        when(equipmentDao.findAvailableEquipmentsByModel(model.getId())).thenReturn(List.of(equipment));

        loanRequestService.resolveOverdueConflicts();

        // Verify r2 is rejected (motived, history saved, loan record deleted)
        assertTrue(r2.getRejectionReason().contains("Annulation automatique"));
        verify(loanRequestDao, times(1)).save(r2);
        verify(requestHistoryDao, times(1)).save(any(RequestHistory.class));
        verify(loanDao, times(1)).delete(l2);
    }
}
