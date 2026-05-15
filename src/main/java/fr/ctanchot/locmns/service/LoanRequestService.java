package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.dto.LoanRequestDTO;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public LoanRequest save(LoanRequest loanRequest) {
        if (loanRequest.getId() == null) {
            loanRequest.setDate(LocalDateTime.now());
        }
        return loanRequestDao.save(loanRequest);
    }

    public void deleteById(Long id) {
        loanRequestDao.deleteById(id);
    }

    public LoanRequest update(Long id, LoanRequestDTO loanRequestUpdate) {
        Optional<LoanRequest> existingRequest = loanRequestDao.findById(id);
        if (existingRequest.isPresent()) {
            LoanRequest requestToUpdate = existingRequest.get();
            
            requestToUpdate.setDescription(loanRequestUpdate.getDescription());

            if (loanRequestUpdate.getAppUserId() != null) {
                Optional<AppUser> user = appUserDao.findById(loanRequestUpdate.getAppUserId());
                user.ifPresent(requestToUpdate::setAppUser);
            }

            if (loanRequestUpdate.getEquipmentId() != null) {
                Optional<Equipment> equipment = equipmentDao.findById(loanRequestUpdate.getEquipmentId());
                equipment.ifPresent(requestToUpdate::setEquipment);
            }
            
            // On ne touche pas à la date originale de la demande
            
            return loanRequestDao.save(requestToUpdate);
        }
        return null;
    }
}
