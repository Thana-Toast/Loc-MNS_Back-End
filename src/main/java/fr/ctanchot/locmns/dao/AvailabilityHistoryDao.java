package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.AvailabilityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityHistoryDao extends JpaRepository<AvailabilityHistory, Long> {
}
