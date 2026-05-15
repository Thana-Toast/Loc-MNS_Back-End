package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.TicketDao;
import fr.ctanchot.locmns.dao.TicketHistoryDao;
import fr.ctanchot.locmns.dto.TicketDTO;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketDao ticketDao;
    private final TicketHistoryDao ticketHistoryDao;
    private final AppUserDao appUserDao;
    private final EquipmentDao equipmentDao;

    @Autowired
    public TicketService(TicketDao ticketDao, TicketHistoryDao ticketHistoryDao, AppUserDao appUserDao, EquipmentDao equipmentDao) {
        this.ticketDao = ticketDao;
        this.ticketHistoryDao = ticketHistoryDao;
        this.appUserDao = appUserDao;
        this.equipmentDao = equipmentDao;
    }

    public List<Ticket> findAll() {
        return ticketDao.findAll();
    }

    public Optional<Ticket> findById(Long id) {
        return ticketDao.findById(id);
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setDate(LocalDateTime.now());
        }
        
        Ticket savedTicket = ticketDao.save(ticket);

        if (ticket.getId() == null || ticketHistoryDao.findAll().stream().noneMatch(h -> h.getTicket().getId().equals(savedTicket.getId()))) {
            TicketHistory history = new TicketHistory();
            history.setTicket(savedTicket);
            history.setStatus(TicketStatus.PENDING);
            history.setStateChangeDate(LocalDateTime.now());
            ticketHistoryDao.save(history);
        }

        return savedTicket;
    }

    public void deleteById(Long id) {
        ticketDao.deleteById(id);
    }

    public Ticket update(Long id, TicketDTO ticketUpdate) {
        Optional<Ticket> existingTicketOpt = ticketDao.findById(id);
        if (existingTicketOpt.isPresent()) {
            Ticket ticketToUpdate = existingTicketOpt.get();
            
            ticketToUpdate.setLabel(ticketUpdate.getLabel());
            ticketToUpdate.setDescription(ticketUpdate.getDescription());

            if (ticketUpdate.getAppUserId() != null) {
                Optional<AppUser> user = appUserDao.findById(ticketUpdate.getAppUserId());
                user.ifPresent(ticketToUpdate::setAppUser);
            }

            if (ticketUpdate.getEquipmentId() != null) {
                Optional<Equipment> equipment = equipmentDao.findById(ticketUpdate.getEquipmentId());
                equipment.ifPresent(ticketToUpdate::setEquipment);
            }
            
            // On ne touche pas à la date originale du ticket
            
            return ticketDao.save(ticketToUpdate);
        }
        return null;
    }

    @Transactional
    public void changeStatus(Long ticketId, TicketStatus newStatus) {
        Optional<Ticket> ticket = ticketDao.findById(ticketId);
        if (ticket.isPresent()) {
            TicketHistory history = new TicketHistory();
            history.setTicket(ticket.get());
            history.setStatus(newStatus);
            history.setStateChangeDate(LocalDateTime.now());
            ticketHistoryDao.save(history);
        }
    }
}
