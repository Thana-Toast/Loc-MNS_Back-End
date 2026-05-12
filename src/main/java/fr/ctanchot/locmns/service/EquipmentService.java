package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentDao equipmentDao;

    @Autowired
    public EquipmentService(EquipmentDao equipmentDao) {
        this.equipmentDao = equipmentDao;
    }

    public List<Equipment> findAll() {
        return equipmentDao.findAll();
    }

    public Optional<Equipment> findById(Long id) {
        return equipmentDao.findById(id);
    }

    public Equipment save(Equipment equipment) {
        return equipmentDao.save(equipment);
    }

    public void deleteById(Long id) {
        equipmentDao.deleteById(id);
    }

    public Equipment update(Long id, Equipment equipmentUpdate) {
        Optional<Equipment> existingEquipment = equipmentDao.findById(id);
        if (existingEquipment.isPresent()) {
            equipmentUpdate.setId(id);
            return equipmentDao.save(equipmentUpdate);
        }
        return null;
    }
}
