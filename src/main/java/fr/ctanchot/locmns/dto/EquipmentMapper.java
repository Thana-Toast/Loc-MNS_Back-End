package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.Model;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

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
            Model model = new Model();
            model.setId(dto.getModelId());
            entity.setModel(model);
        }

        return entity;
    }
}

