package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class TicketMapper {

    public TicketDTO toDto(Ticket entity) {
        if (entity == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setLabel(entity.getLabel());
        dto.setDescription(entity.getDescription());

        if (entity.getEquipment() != null) {
            dto.setEquipmentId(entity.getEquipment().getId());
            dto.setEquipmentSerialNumber(entity.getEquipment().getSerialNumber());
        }

        if (entity.getAppUser() != null) {
            dto.setAppUserId(entity.getAppUser().getId());
            dto.setAppUserFullName(entity.getAppUser().getFirstName() + " " + entity.getAppUser().getLastName());
        }

        // Récupération du statut actuel via l'historique le plus récent sans DAO
        if (entity.getHistories() != null && !entity.getHistories().isEmpty()) {
            entity.getHistories().stream()
                    .max(Comparator.comparing(TicketHistory::getStateChangeDate))
                    .ifPresent(latestHistory -> dto.setCurrentStatus(latestHistory.getStatus()));
        }

        return dto;
    }

    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) return null;

        Ticket entity = new Ticket();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setLabel(dto.getLabel());
        entity.setDescription(dto.getDescription());

        if (dto.getEquipmentId() != null) {
            Equipment equipment = new Equipment();
            equipment.setId(dto.getEquipmentId());
            entity.setEquipment(equipment);
        }

        if (dto.getAppUserId() != null) {
            AppUser user = new AppUser();
            user.setId(dto.getAppUserId());
            entity.setAppUser(user);
        }

        return entity;
    }
}

