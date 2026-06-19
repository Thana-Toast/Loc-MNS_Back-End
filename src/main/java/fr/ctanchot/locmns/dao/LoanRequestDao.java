package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRequestDao extends JpaRepository<LoanRequest, Long> {

    @Query("SELECT r FROM LoanRequest r WHERE r.appUser.id = :userId")
    List<LoanRequest> findByAppUserId(@Param("userId") Long userId);
}
