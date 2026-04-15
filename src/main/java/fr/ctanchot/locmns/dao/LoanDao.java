package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDao extends JpaRepository<Loan, Long> {
}
