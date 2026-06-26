package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.LoanDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.dao.RequestHistoryDao;
import fr.ctanchot.locmns.dto.LoanDTO;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanDao loanDao;
    private final LoanRequestDao loanRequestDao;
    private final RequestHistoryDao requestHistoryDao;
    private final AppUserDao appUserDao;
    private final LoanRequestService loanRequestService;

    @Autowired
    public LoanService(LoanDao loanDao, LoanRequestDao loanRequestDao, RequestHistoryDao requestHistoryDao, AppUserDao appUserDao, LoanRequestService loanRequestService) {
        this.loanDao = loanDao;
        this.loanRequestDao = loanRequestDao;
        this.requestHistoryDao = requestHistoryDao;
        this.appUserDao = appUserDao;
        this.loanRequestService = loanRequestService;
    }

    private Optional<AppUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return appUserDao.findByEmail(email);
        }
        return Optional.empty();
    }

    @Transactional
    public List<Loan> findAll() {
        loanRequestService.resolveOverdueConflicts();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return loanDao.findAll();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return loanDao.findAll();
        }

        return getCurrentUser()
                .map(user -> loanDao.findByAppUserId(user.getId()))
                .orElse(List.of());
    }

    public Optional<Loan> findById(Long id) {
        Optional<Loan> loanOpt = loanDao.findById(id);
        if (loanOpt.isEmpty()) {
            return Optional.empty();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return loanOpt;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return loanOpt;
        }

        Optional<AppUser> currentUserOpt = getCurrentUser();
        if (currentUserOpt.isPresent() && loanOpt.get().getLoanRequest().getAppUser().getId().equals(currentUserOpt.get().getId())) {
            return loanOpt;
        }

        return Optional.empty();
    }

    @Transactional
    public Loan save(Loan loan) {
        if (isEquipmentAvailable(loan)) {
            Loan savedLoan = loanDao.save(loan);

            // Add VALIDATED status in history for the associated LoanRequest
            LoanRequest request = loanRequestDao.findById(loan.getLoanRequest().getId())
                    .orElseThrow(() -> new RuntimeException("Demande de prêt d'origine introuvable."));

            RequestHistory history = new RequestHistory();
            history.setLoanRequest(request);
            history.setStatusChangeDate(LocalDateTime.now());
            history.setStatus(RequestStatus.VALIDATED);
            requestHistoryDao.save(history);

            return savedLoan;
        } else {
            throw new RuntimeException("L'équipement n'est pas disponible pour cette période (conflit de dates).");
        }
    }

    public void deleteById(Long id) {
        loanDao.deleteById(id);
    }

    @Transactional
    public Loan update(Long id, LoanDTO loanUpdate) {
        Optional<Loan> existingLoanOpt = loanDao.findById(id);
        if (existingLoanOpt.isPresent()) {
            Loan loanToUpdate = existingLoanOpt.get();
            
            loanToUpdate.setStartDate(loanUpdate.getStartDate());
            loanToUpdate.setEndDate(loanUpdate.getEndDate());
            loanToUpdate.setActualReturnDate(loanUpdate.getActualReturnDate());

            if (loanUpdate.getLoanRequestId() != null) {
                Optional<LoanRequest> request = loanRequestDao.findById(loanUpdate.getLoanRequestId());
                request.ifPresent(loanToUpdate::setLoanRequest);
            }

            if (isEquipmentAvailable(loanToUpdate)) {
                return loanDao.save(loanToUpdate);
            } else {
                throw new RuntimeException("Mise à jour impossible : conflit de dates pour cet équipement.");
            }
        }
        return null;
    }

    private boolean isEquipmentAvailable(Loan newLoan) {
        LoanRequest request = newLoan.getLoanRequest();
        if (request == null) {
            return true;
        }

        // Si le request ne contient pas encore d'équipement, on le charge
        LoanRequest fullRequest = loanRequestDao.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Demande de prêt d'origine introuvable."));

        if (fullRequest.getEquipment() == null) {
            return true;
        }

        Long equipmentId = fullRequest.getEquipment().getId();
        LocalDate start = newLoan.getStartDate();
        LocalDate end = newLoan.getEndDate();

        // 1-day buffer check (subtract 1 day from start, add 1 day to end)
        boolean hasOverlap = loanDao.existsOverlappingLoan(
                equipmentId,
                newLoan.getId(),
                start.minusDays(1),
                end.plusDays(1)
        );
        if (hasOverlap) {
            return false;
        }

        // Overdue check (prevent new loans on equipment that is currently overdue)
        boolean hasOverdue = loanDao.existsActiveOverdueLoan(
                equipmentId,
                newLoan.getId(),
                LocalDate.now()
        );
        if (hasOverdue) {
            return false;
        }

        return true;
    }

    public List<Loan> findActiveLoansByModelId(Long modelId) {
        return loanDao.findActiveLoansByModelId(modelId);
    }
}
