package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDao extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.appUser.id = :userId")
    List<Ticket> findByAppUserId(@Param("userId") Long userId);
}

