package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.DocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocTypeDao extends JpaRepository<DocType, Long> {
}
