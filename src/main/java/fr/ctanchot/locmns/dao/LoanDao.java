package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanDao extends JpaRepository<Loan, Long> {

    @Query("SELECT COUNT(l) > 0 FROM Loan l " +
           "WHERE l.loanRequest.equipment.id = :equipmentId " +
           "AND (:loanId IS NULL OR l.id != :loanId) " +
           "AND l.startDate <= :endDate " +
           "AND l.endDate >= :startDate")
    boolean existsOverlappingLoan(
        @Param("equipmentId") Long equipmentId,
        @Param("loanId") Long loanId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT l FROM Loan l WHERE l.loanRequest.appUser.id = :userId")
    List<Loan> findByAppUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Loan l WHERE l.loanRequest.equipment.model.id = :modelId AND l.actualReturnDate IS NULL")
    List<Loan> findActiveLoansByModelId(@Param("modelId") Long modelId);
}

