package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestHistoryDao extends JpaRepository<RequestHistory, Long> {
}
