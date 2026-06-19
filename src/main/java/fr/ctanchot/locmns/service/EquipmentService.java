package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AvailabilityHistoryDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.dao.WearHistoryDao;
import fr.ctanchot.locmns.dto.EquipmentDTO;
import fr.ctanchot.locmns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentDao equipmentDao;
    private final ModelDao modelDao;
    private final AvailabilityHistoryDao availabilityHistoryDao;
    private final WearHistoryDao wearHistoryDao;

    @Autowired
    public EquipmentService(EquipmentDao equipmentDao, ModelDao modelDao, AvailabilityHistoryDao availabilityHistoryDao, WearHistoryDao wearHistoryDao) {
        this.equipmentDao = equipmentDao;
        this.modelDao = modelDao;
        this.availabilityHistoryDao = availabilityHistoryDao;
        this.wearHistoryDao = wearHistoryDao;
    }

    public List<Equipment> findAll() {
        return equipmentDao.findAll();
    }

    public Optional<Equipment> findById(Long id) {
        return equipmentDao.findById(id);
    }

    @Transactional
    public Equipment save(Equipment equipment) {
        boolean isNew = equipment.getId() == null;
        Equipment savedEquipment = equipmentDao.save(equipment);

        if (isNew) {
            // Save default AvailabilityHistory as IN_RESERVE
            AvailabilityHistory availabilityHistory = new AvailabilityHistory();
            availabilityHistory.setEquipment(savedEquipment);
            availabilityHistory.setStartDate(LocalDateTime.now());
            availabilityHistory.setStatus(AvailabilityStatus.IN_RESERVE);
            availabilityHistoryDao.save(availabilityHistory);

            // Save default WearHistory as NEW
            WearHistory wearHistory = new WearHistory();
            wearHistory.setEquipment(savedEquipment);
            wearHistory.setWearChangeDate(LocalDateTime.now());
            wearHistory.setStatus(WearStatus.NEW);
            wearHistoryDao.save(wearHistory);
        }

        return savedEquipment;
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
