package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.dto.LoanRequestDTO;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanRequestService {

    private final LoanRequestDao loanRequestDao;
    private final AppUserDao appUserDao;
    private final EquipmentDao equipmentDao;

    @Autowired
    public LoanRequestService(LoanRequestDao loanRequestDao, AppUserDao appUserDao, EquipmentDao equipmentDao) {
        this.loanRequestDao = loanRequestDao;
        this.appUserDao = appUserDao;
        this.equipmentDao = equipmentDao;
    }

    public List<LoanRequest> findAll() {
        return loanRequestDao.findAll();
    }

    public Optional<LoanRequest> findById(Long id) {
        return loanRequestDao.findById(id);
    }

    @Transactional
    public LoanRequest save(LoanRequest loanRequest) {
        if (loanRequest.getId() == null) {
            loanRequest.setDate(LocalDateTime.now());
        }

        if (loanRequest.getAppUser() == null || loanRequest.getAppUser().getId() == null) {
            throw new RuntimeException("L'utilisateur demandeur est obligatoire.");
        }
        if (loanRequest.getEquipment() == null || loanRequest.getEquipment().getId() == null) {
            throw new RuntimeException("L'équipement demandé est obligatoire.");
        }

        AppUser user = appUserDao.findById(loanRequest.getAppUser().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
        
        Equipment equipment = equipmentDao.findById(loanRequest.getEquipment().getId())
                .orElseThrow(() -> new RuntimeException("Équipement introuvable."));

        loanRequest.setAppUser(user);
        loanRequest.setEquipment(equipment);

        validateCategoryAccess(user, equipment);

        return loanRequestDao.save(loanRequest);
    }

    public void deleteById(Long id) {
        loanRequestDao.deleteById(id);
    }

    @Transactional
    public LoanRequest update(Long id, LoanRequestDTO loanRequestUpdate) {
        Optional<LoanRequest> existingRequest = loanRequestDao.findById(id);
        if (existingRequest.isPresent()) {
            LoanRequest requestToUpdate = existingRequest.get();
            
            requestToUpdate.setDescription(loanRequestUpdate.getDescription());

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

    private void validateCategoryAccess(AppUser user, Equipment equipment) {
        Category targetCategory = equipment.getModel().getCategory();
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new RuntimeException("L'utilisateur n'a aucun rôle associé.");
        }
        
        boolean hasAccess = user.getRoles().stream()
                .flatMap(role -> role.getAuthorizedCategories().stream())
                .anyMatch(category -> category.getId().equals(targetCategory.getId()));

        if (!hasAccess) {
            throw new RuntimeException("L'utilisateur n'a pas les droits pour louer des équipements de la catégorie : " + targetCategory.getLabel());
        }
    }
}

