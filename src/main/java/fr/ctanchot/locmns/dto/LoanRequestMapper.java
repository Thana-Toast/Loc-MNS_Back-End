package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.LoanRequest;
import fr.ctanchot.locmns.model.RequestHistory;
import fr.ctanchot.locmns.model.RequestStatus;
import org.springframework.stereotype.Component;
import java.util.Comparator;

@Component
public class LoanRequestMapper {

    public LoanRequestDTO toDto(LoanRequest entity) {
        if (entity == null) return null;

        LoanRequestDTO dto = new LoanRequestDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        dto.setRejectionReason(entity.getRejectionReason());

        if (entity.getAppUser() != null) {
            dto.setAppUserId(entity.getAppUser().getId());
            dto.setAppUserFullName(entity.getAppUser().getFirstName() + " " + entity.getAppUser().getLastName());
            dto.setAppUserEmail(entity.getAppUser().getEmail());
            if (entity.getAppUser().getRoles() != null) {
                dto.setAppUserRoles(entity.getAppUser().getRoles().stream()
                        .map(fr.ctanchot.locmns.model.Role::getLabel)
                        .toList());
            }
        }

        if (entity.getEquipment() != null) {
            dto.setEquipmentId(entity.getEquipment().getId());
            dto.setEquipmentSerialNumber(entity.getEquipment().getSerialNumber());
            if (entity.getEquipment().getModel() != null) {
                dto.setModelId(entity.getEquipment().getModel().getId());
                dto.setModelLabel(entity.getEquipment().getModel().getLabel());
                dto.setModelBrand(entity.getEquipment().getModel().getBrand());
                dto.setModelDescription(entity.getEquipment().getModel().getDescription());
                dto.setModelTechnicalSpecs(entity.getEquipment().getModel().getTechnicalSpecs());
                dto.setModelImagePath(entity.getEquipment().getModel().getImagePath());
            }
        }

        if (entity.getHistories() != null && !entity.getHistories().isEmpty()) {
            RequestStatus latestStatus = entity.getHistories().stream()
                    .max(Comparator.comparing(RequestHistory::getStatusChangeDate))
                    .map(RequestHistory::getStatus)
                    .orElse(RequestStatus.PENDING);
            dto.setStatus(latestStatus.name());
        } else {
            dto.setStatus(RequestStatus.PENDING.name());
        }

        if (entity.getLoan() != null) {
            dto.setLoanId(entity.getLoan().getId());
            dto.setLoanStartDate(entity.getLoan().getStartDate());
            dto.setLoanEndDate(entity.getLoan().getEndDate());
            dto.setLoanActualReturnDate(entity.getLoan().getActualReturnDate());
            dto.setIsApproved(true);
        } else {
            dto.setIsApproved(false);
        }

        return dto;
    }

    public LoanRequest toEntity(LoanRequestDTO dto) {
        if (dto == null) return null;

        LoanRequest entity = new LoanRequest();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

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

