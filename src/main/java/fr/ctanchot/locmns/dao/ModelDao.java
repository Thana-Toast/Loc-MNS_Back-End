package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelDao extends JpaRepository<Model, Long> {

    @Query("SELECT DISTINCT m FROM Model m JOIN m.category c " +
           "WHERE c.rolesWithAccess IS EMPTY " +
           "OR EXISTS (SELECT r FROM c.rolesWithAccess r WHERE r.label IN :roles)")
    List<Model> findModelsByAuthorizedCategories(@Param("roles") List<String> roles);

    @Query("SELECT m.imagePath FROM Model m WHERE m.imagePath IS NOT NULL")
    List<String> findAllImagePaths();
}
