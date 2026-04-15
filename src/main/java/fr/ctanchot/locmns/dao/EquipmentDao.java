package fr.ctanchot.locmns.dao;

import fr.ctanchot.locmns.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentDao extends JpaRepository<Equipment, Long> {
}
