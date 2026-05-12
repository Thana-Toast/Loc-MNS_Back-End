package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.TicketDao;
import fr.ctanchot.locmns.dao.TicketHistoryDao;
import fr.ctanchot.locmns.model.Ticket;
import fr.ctanchot.locmns.model.TicketHistory;
import fr.ctanchot.locmns.model.TicketStatus;
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

    @Autowired
    public TicketService(TicketDao ticketDao, TicketHistoryDao ticketHistoryDao) {
        this.ticketDao = ticketDao;
        this.ticketHistoryDao = ticketHistoryDao;
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

    public Ticket update(Long id, Ticket ticketUpdate) {
        Optional<Ticket> existingTicket = ticketDao.findById(id);
        if (existingTicket.isPresent()) {
            ticketUpdate.setId(id);
            ticketUpdate.setDate(existingTicket.get().getDate());
            return ticketDao.save(ticketUpdate);
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
