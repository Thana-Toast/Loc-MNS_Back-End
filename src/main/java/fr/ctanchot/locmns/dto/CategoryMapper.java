package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category entity) {
        if (entity == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());
        dto.setDescription(entity.getDescription());

        if (entity.getParentCategory() != null) {
            dto.setParentCategoryId(entity.getParentCategory().getId());
            dto.setParentCategoryLabel(entity.getParentCategory().getLabel());
        }

        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;

        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());
        entity.setDescription(dto.getDescription());

        if (dto.getParentCategoryId() != null) {
            Category parent = new Category();
            parent.setId(dto.getParentCategoryId());
            entity.setParentCategory(parent);
        }

        return entity;
    }
}

