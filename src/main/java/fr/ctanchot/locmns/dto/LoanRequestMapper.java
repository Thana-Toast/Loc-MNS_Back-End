package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestMapper {

    private final AppUserDao appUserDao;
    private final EquipmentDao equipmentDao;

    @Autowired
    public LoanRequestMapper(AppUserDao appUserDao, EquipmentDao equipmentDao) {
        this.appUserDao = appUserDao;
        this.equipmentDao = equipmentDao;
    }

    public LoanRequestDTO toDto(LoanRequest entity) {
        if (entity == null) return null;

        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setDescription(entity.getDescription());

        if (entity.getAppUser() != null) {
            dto.setAppUserId(entity.getAppUser().getId());
            dto.setAppUserFullName(entity.getAppUser().getFirstName() + " " + entity.getAppUser().getLastName());
        }

        if (entity.getEquipment() != null) {
            dto.setEquipmentId(entity.getEquipment().getId());
            dto.setEquipmentSerialNumber(entity.getEquipment().getSerialNumber());
        }

        return dto;
    }

    public LoanRequest toEntity(LoanRequestDTO dto) {
        if (dto == null) return null;

        LoanRequest entity = new LoanRequest();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());

        if (dto.getAppUserId() != null) {
            AppUser user = appUserDao.findById(dto.getAppUserId()).orElse(null);
            entity.setAppUser(user);
        }

        if (dto.getEquipmentId() != null) {
            Equipment equipment = equipmentDao.findById(dto.getEquipmentId()).orElse(null);
            entity.setEquipment(equipment);
        }

        return entity;
    }
}
