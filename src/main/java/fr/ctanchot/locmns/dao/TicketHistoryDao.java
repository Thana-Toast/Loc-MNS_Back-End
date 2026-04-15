package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketHistoryDao extends JpaRepository<TicketHistory, Long> {
}
