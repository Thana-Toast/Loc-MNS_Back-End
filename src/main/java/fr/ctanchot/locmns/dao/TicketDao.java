package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends JpaRepository<Ticket, Long> {
}
