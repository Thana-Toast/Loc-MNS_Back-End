package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.LoanDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.dao.RequestHistoryDao;
import fr.ctanchot.locmns.dto.LoanRequestDTO;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LoanRequestService {

    private final LoanRequestDao loanRequestDao;
    private final AppUserDao appUserDao;
    private final EquipmentDao equipmentDao;
    private final RequestHistoryDao requestHistoryDao;
    private final LoanDao loanDao;

    @Autowired
    public LoanRequestService(LoanRequestDao loanRequestDao, AppUserDao appUserDao, EquipmentDao equipmentDao, RequestHistoryDao requestHistoryDao, LoanDao loanDao) {
        this.loanRequestDao = loanRequestDao;
        this.appUserDao = appUserDao;
        this.equipmentDao = equipmentDao;
        this.requestHistoryDao = requestHistoryDao;
        this.loanDao = loanDao;
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
    public List<LoanRequest> findAll() {
        resolveOverdueConflicts();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return loanRequestDao.findAll();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return loanRequestDao.findAll();
        }

        return getCurrentUser()
                .map(user -> loanRequestDao.findByAppUserId(user.getId()))
                .orElse(List.of());
    }

    public Optional<LoanRequest> findById(Long id) {
        Optional<LoanRequest> requestOpt = loanRequestDao.findById(id);
        if (requestOpt.isEmpty()) {
            return Optional.empty();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return requestOpt;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return requestOpt;
        }

        Optional<AppUser> currentUserOpt = getCurrentUser();
        if (currentUserOpt.isPresent() && requestOpt.get().getAppUser().getId().equals(currentUserOpt.get().getId())) {
            return requestOpt;
        }

        return Optional.empty();
    }

    @Transactional
    public LoanRequest save(LoanRequest loanRequest, Long modelId) {
        validateDates(loanRequest.getStartDate(), loanRequest.getEndDate());
        boolean isNew = loanRequest.getId() == null;
        if (isNew) {
            loanRequest.setDate(LocalDateTime.now());
        }

        AppUser user;
        if (loanRequest.getAppUser() == null || loanRequest.getAppUser().getId() == null) {
            user = getCurrentUser()
                    .orElseThrow(() -> new RuntimeException("Utilisateur non authentifié ou introuvable."));
        } else {
            user = appUserDao.findById(loanRequest.getAppUser().getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
        }
        loanRequest.setAppUser(user);

        Equipment equipment;
        if (loanRequest.getEquipment() == null || loanRequest.getEquipment().getId() == null) {
            if (modelId == null) {
                throw new RuntimeException("L'équipement ou le modèle demandé est obligatoire.");
            }

            // Find all equipments of this model whose current status is AVAILABLE.
            List<Equipment> candidates = equipmentDao.findAvailableEquipmentsByModel(modelId);

            // Find the first candidate that doesn't have an overlapping loan.
            Equipment selectedEquipment = null;
            for (Equipment candidate : candidates) {
                // Check if candidate has any active overdue loans
                boolean hasOverdue = loanDao.existsActiveOverdueLoan(
                        candidate.getId(),
                        null,
                        LocalDate.now()
                );
                if (hasOverdue) {
                    continue;
                }

                // Check overlap with 1-day buffer margin
                boolean hasOverlap = loanDao.existsOverlappingLoan(
                        candidate.getId(),
                        null,
                        loanRequest.getStartDate().minusDays(1),
                        loanRequest.getEndDate().plusDays(1)
                );
                if (!hasOverlap) {
                    selectedEquipment = candidate;
                    break;
                }
            }

            if (selectedEquipment == null) {
                throw new RuntimeException("Aucun équipement disponible de ce modèle pour les dates sélectionnées.");
            }
            equipment = selectedEquipment;
        } else {
            equipment = equipmentDao.findById(loanRequest.getEquipment().getId())
                    .orElseThrow(() -> new RuntimeException("Équipement introuvable."));
        }

        loanRequest.setEquipment(equipment);
        validateCategoryAccess(user, equipment);

        LoanRequest savedRequest = loanRequestDao.save(loanRequest);

        if (isNew) {
            // Save RequestHistory as PENDING
            RequestHistory history = new RequestHistory();
            history.setLoanRequest(savedRequest);
            history.setStatusChangeDate(LocalDateTime.now());
            history.setStatus(RequestStatus.PENDING);
            requestHistoryDao.save(history);
        }

        return savedRequest;
    }

    public void deleteById(Long id) {
        loanRequestDao.deleteById(id);
    }

    @Transactional
    public LoanRequest update(Long id, LoanRequestDTO loanRequestUpdate) {
        Optional<LoanRequest> existingRequest = loanRequestDao.findById(id);
        if (existingRequest.isPresent()) {
            LoanRequest requestToUpdate = existingRequest.get();
            
            validateDates(loanRequestUpdate.getStartDate(), loanRequestUpdate.getEndDate());
            requestToUpdate.setDescription(loanRequestUpdate.getDescription());
            requestToUpdate.setStartDate(loanRequestUpdate.getStartDate());
            requestToUpdate.setEndDate(loanRequestUpdate.getEndDate());

            AppUser user = requestToUpdate.getAppUser();
            if (loanRequestUpdate.getAppUserId() != null) {
                user = appUserDao.findById(loanRequestUpdate.getAppUserId())
                        .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
                requestToUpdate.setAppUser(user);
            }

            Equipment equipment = requestToUpdate.getEquipment();
            if (loanRequestUpdate.getEquipmentId() != null) {
                equipment = equipmentDao.findById(loanRequestUpdate.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("Équipement introuvable."));
                requestToUpdate.setEquipment(equipment);
            }
            
            validateCategoryAccess(user, equipment);
            
            return loanRequestDao.save(requestToUpdate);
        }
        return null;
    }

    @Transactional
    public void reject(Long id, String reason) {
        LoanRequest request = loanRequestDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande de prêt introuvable."));

        // Vérifier si elle est déjà validée
        boolean isAlreadyValidated = request.getHistories().stream()
                .max(Comparator.comparing(RequestHistory::getStatusChangeDate))
                .map(h -> h.getStatus() == RequestStatus.VALIDATED)
                .orElse(false);

        if (isAlreadyValidated) {
            throw new RuntimeException("Impossible de refuser une demande déjà validée.");
        }

        request.setRejectionReason(reason);
        loanRequestDao.save(request);

        // Add REJECTED state in history
        RequestHistory history = new RequestHistory();
        history.setLoanRequest(request);
        history.setStatusChangeDate(LocalDateTime.now());
        history.setStatus(RequestStatus.REJECTED);
        requestHistoryDao.save(history);
    }

    private void validateCategoryAccess(AppUser user, Equipment equipment) {
        Category targetCategory = equipment.getModel().getCategory();
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new RuntimeException("L'utilisateur n'a aucun rôle associé.");
        }
        
        // Open by default logic
        if (targetCategory.getRolesWithAccess().isEmpty()) {
            return;
        }
        
        boolean hasAccess = user.getRoles().stream()
                .anyMatch(role -> targetCategory.getRolesWithAccess().stream()
                        .anyMatch(roleWithAccess -> roleWithAccess.getId().equals(role.getId())));

        if (!hasAccess) {
            throw new RuntimeException("L'utilisateur n'a pas les droits pour louer des équipements de la catégorie : " + targetCategory.getLabel());
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("Les dates de début et de fin sont obligatoires.");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("La date de début souhaitée ne peut pas être dans le passé.");
        }
        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("La date de fin ne peut pas être antérieure à la date de début.");
        }
    }

    @Transactional
    public void resolveOverdueConflicts() {
        LocalDate today = LocalDate.now();
        List<Loan> overdueLoans = loanDao.findOverdueLoans(today);
        
        for (Loan l1 : overdueLoans) {
            Equipment eq = l1.getLoanRequest().getEquipment();
            List<Loan> conflictingLoans = loanDao.findConflictingLoans(eq.getId(), today, l1.getId());
            
            for (Loan l2 : conflictingLoans) {
                // Tenter de réattribuer l2 à un autre exemplaire du même modèle
                Long modelId = eq.getModel().getId();
                List<Equipment> candidates = equipmentDao.findAvailableEquipmentsByModel(modelId);
                Equipment replacement = null;
                
                for (Equipment candidate : candidates) {
                    if (candidate.getId().equals(eq.getId())) {
                        continue;
                    }
                    
                    // Vérifier si le candidat a un prêt en retard
                    boolean hasOverdue = loanDao.existsActiveOverdueLoan(candidate.getId(), null, today);
                    if (hasOverdue) {
                        continue;
                    }
                    
                    // Vérifier si le candidat a un prêt chevauchant sur la plage de l2 (+ 1 jour de maintenance)
                    boolean hasOverlap = loanDao.existsOverlappingLoan(
                            candidate.getId(),
                            l2.getId(),
                            l2.getStartDate().minusDays(1),
                            l2.getEndDate().plusDays(1)
                    );
                    if (!hasOverlap) {
                        replacement = candidate;
                        break;
                    }
                }
                
                LoanRequest req2 = l2.getLoanRequest();
                if (replacement != null) {
                    // Réattribuer l2
                    req2.setEquipment(replacement);
                    loanRequestDao.save(req2);
                } else {
                    // Plus aucun exemplaire disponible -> Annulation automatique de l2
                    req2.setRejectionReason("Annulation automatique : le matériel précédent n'a pas été restitué à temps et aucun autre exemplaire n'est disponible.");
                    loanRequestDao.save(req2);
                    
                    // Ajouter le statut REJECTED dans l'historique
                    RequestHistory history = new RequestHistory();
                    history.setLoanRequest(req2);
                    history.setStatusChangeDate(LocalDateTime.now());
                    history.setStatus(RequestStatus.REJECTED);
                    requestHistoryDao.save(history);
                    
                    // Supprimer le prêt validé
                    loanDao.delete(l2);
                }
            }
        }
    }
}

