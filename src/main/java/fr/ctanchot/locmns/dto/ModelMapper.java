package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.dao.CategoryDao;
import fr.ctanchot.locmns.model.Category;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    private final CategoryDao categoryDao;

    @Autowired
    public ModelMapper(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

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
            Category category = categoryDao.findById(dto.getCategoryId()).orElse(null);
            entity.setCategory(category);
        }

        return entity;
    }
}
