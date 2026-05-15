package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.dto.EquipmentDTO;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentDao equipmentDao;
    private final ModelDao modelDao;

    @Autowired
    public EquipmentService(EquipmentDao equipmentDao, ModelDao modelDao) {
        this.equipmentDao = equipmentDao;
        this.modelDao = modelDao;
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

    public Equipment update(Long id, EquipmentDTO equipmentUpdate) {
        Optional<Equipment> existingEquipment = equipmentDao.findById(id);
        if (existingEquipment.isPresent()) {
            Equipment equipmentToUpdate = existingEquipment.get();
            
            equipmentToUpdate.setSerialNumber(equipmentUpdate.getSerialNumber());

            if (equipmentUpdate.getModelId() != null) {
                Optional<Model> model = modelDao.findById(equipmentUpdate.getModelId());
                model.ifPresent(equipmentToUpdate::setModel);
            }
            
            return equipmentDao.save(equipmentToUpdate);
        }
        return null;
    }
}
