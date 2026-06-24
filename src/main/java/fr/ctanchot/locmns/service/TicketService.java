package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.TicketDao;
import fr.ctanchot.locmns.dao.TicketHistoryDao;
import fr.ctanchot.locmns.dto.TicketDTO;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public List<Ticket> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ticketDao.findAll();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ticketDao.findAll();
        }

        return getCurrentUser()
                .map(user -> ticketDao.findByAppUserId(user.getId()))
                .orElse(List.of());
    }

    public Optional<Ticket> findById(Long id) {
        Optional<Ticket> ticketOpt = ticketDao.findById(id);
        if (ticketOpt.isEmpty()) {
            return Optional.empty();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ticketOpt;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ticketOpt;
        }

        Optional<AppUser> currentUserOpt = getCurrentUser();
        if (currentUserOpt.isPresent() && ticketOpt.get().getAppUser().getId().equals(currentUserOpt.get().getId())) {
            return ticketOpt;
        }

        return Optional.empty();
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setDate(LocalDateTime.now());
        }
        
        if (ticket.getAppUser() == null || ticket.getAppUser().getId() == null) {
            AppUser user = getCurrentUser()
                    .orElseThrow(() -> new RuntimeException("Utilisateur non authentifié ou introuvable."));
            ticket.setAppUser(user);
        } else {
            AppUser user = appUserDao.findById(ticket.getAppUser().getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
            ticket.setAppUser(user);
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
