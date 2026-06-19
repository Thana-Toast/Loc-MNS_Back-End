package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {

    @Query("SELECT DISTINCT c FROM Category c " +
           "WHERE c.rolesWithAccess IS EMPTY " +
           "OR EXISTS (SELECT r FROM c.rolesWithAccess r WHERE r.label IN :roles)")
    List<Category> findAuthorizedCategories(@Param("roles") List<String> roles);
}
