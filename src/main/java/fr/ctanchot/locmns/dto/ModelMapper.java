package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.Category;
import fr.ctanchot.locmns.model.Model;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public ModelDTO toDto(Model entity) {
        if (entity == null) return null;

        ModelDTO dto = new ModelDTO();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());
        dto.setBrand(entity.getBrand());
        dto.setDescription(entity.getDescription());
        dto.setTechnicalSpecs(entity.getTechnicalSpecs());
        dto.setImagePath(entity.getImagePath());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryLabel(entity.getCategory().getLabel());
        }

        if (entity.getEquipments() != null) {
            dto.setEquipmentsCount(entity.getEquipments().size());
        } else {
            dto.setEquipmentsCount(0);
        }

        return dto;
    }

    public Model toEntity(ModelDTO dto) {
        if (dto == null) return null;

        Model entity = new Model();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());
        entity.setBrand(dto.getBrand());
        entity.setDescription(dto.getDescription());
        entity.setTechnicalSpecs(dto.getTechnicalSpecs());
        entity.setImagePath(dto.getImagePath());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            entity.setCategory(category);
        }

        return entity;
    }
}

