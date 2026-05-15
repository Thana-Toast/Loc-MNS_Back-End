package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.TicketHistoryDao;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class TicketMapper {

    private final EquipmentDao equipmentDao;
    private final AppUserDao appUserDao;
    private final TicketHistoryDao ticketHistoryDao;

    @Autowired
    public TicketMapper(EquipmentDao equipmentDao, AppUserDao appUserDao, TicketHistoryDao ticketHistoryDao) {
        this.equipmentDao = equipmentDao;
        this.appUserDao = appUserDao;
        this.ticketHistoryDao = ticketHistoryDao;
    }

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

        // Récupération du statut actuel via l'historique le plus récent
        if (entity.getId() != null) {
            ticketHistoryDao.findAll().stream()
                    .filter(h -> h.getTicket().getId().equals(entity.getId()))
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
            Equipment equipment = equipmentDao.findById(dto.getEquipmentId()).orElse(null);
            entity.setEquipment(equipment);
        }

        if (dto.getAppUserId() != null) {
            AppUser user = appUserDao.findById(dto.getAppUserId()).orElse(null);
            entity.setAppUser(user);
        }

        return entity;
    }
}
