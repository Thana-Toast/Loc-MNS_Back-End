package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.CategoryDao;
import fr.ctanchot.locmns.dto.CategoryDTO;
import fr.ctanchot.locmns.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryDao.findById(id);
    }

    public Category save(Category category) {
        return categoryDao.save(category);
    }

    public void deleteById(Long id) {
        categoryDao.deleteById(id);
    }

    public Category update(Long id, CategoryDTO categoryUpdate) {
        Optional<Category> existingCategory = categoryDao.findById(id);
        if (existingCategory.isPresent()) {
            Category categoryToUpdate = existingCategory.get();
            
            categoryToUpdate.setLabel(categoryUpdate.getLabel());
            categoryToUpdate.setDescription(categoryUpdate.getDescription());

            if (categoryUpdate.getParentCategoryId() != null) {
                Optional<Category> parent = categoryDao.findById(categoryUpdate.getParentCategoryId());
                parent.ifPresent(categoryToUpdate::setParentCategory);
            } else {
                categoryToUpdate.setParentCategory(null);
            }
            
            return categoryDao.save(categoryToUpdate);
        }
        return null;
    }
}
