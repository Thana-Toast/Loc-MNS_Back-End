package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelDao extends JpaRepository<Model, Long> {
}
