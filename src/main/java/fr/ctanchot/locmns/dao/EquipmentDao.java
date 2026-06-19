package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentDao extends JpaRepository<Equipment, Long> {

    @Query("SELECT e FROM Equipment e WHERE e.model.id = :modelId " +
           "AND EXISTS (SELECT h FROM AvailabilityHistory h WHERE h.equipment.id = e.id AND h.endDate IS NULL AND h.status = fr.ctanchot.locmns.model.AvailabilityStatus.AVAILABLE)")
    List<Equipment> findAvailableEquipmentsByModel(@Param("modelId") Long modelId);
}
