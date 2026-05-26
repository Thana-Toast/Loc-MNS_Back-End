package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestMapper {

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
            AppUser user = new AppUser();
            user.setId(dto.getAppUserId());
            entity.setAppUser(user);
        }

        if (dto.getEquipmentId() != null) {
            Equipment equipment = new Equipment();
            equipment.setId(dto.getEquipmentId());
            entity.setEquipment(equipment);
        }

        return entity;
    }
}

