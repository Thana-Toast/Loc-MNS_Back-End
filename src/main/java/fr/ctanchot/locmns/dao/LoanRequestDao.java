package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRequestDao extends JpaRepository<LoanRequest, Long> {
}
