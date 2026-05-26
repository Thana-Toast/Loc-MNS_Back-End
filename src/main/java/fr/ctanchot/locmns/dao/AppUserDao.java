package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserDao extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}

