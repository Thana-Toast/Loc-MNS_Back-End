package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    private final ModelDao modelDao;

    @Autowired
    public EquipmentMapper(ModelDao modelDao) {
        this.modelDao = modelDao;
    }

    public EquipmentDTO toDto(Equipment entity) {
        if (entity == null) return null;

        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(entity.getId());
        dto.setSerialNumber(entity.getSerialNumber());
        
        if (entity.getModel() != null) {
            dto.setModelId(entity.getModel().getId());
            dto.setModelLabel(entity.getModel().getLabel());
        }

        return dto;
    }

    public Equipment toEntity(EquipmentDTO dto) {
        if (dto == null) return null;

        Equipment entity = new Equipment();
        entity.setId(dto.getId());
        entity.setSerialNumber(dto.getSerialNumber());

        if (dto.getModelId() != null) {
            Model model = modelDao.findById(dto.getModelId()).orElse(null);
            entity.setModel(model);
        }

        return entity;
    }
}
