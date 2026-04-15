package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.WearHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WearHistoryDao extends JpaRepository<WearHistory, Long> {
}
