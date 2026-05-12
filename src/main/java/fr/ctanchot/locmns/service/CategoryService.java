package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.CategoryDao;
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

    public Category update(Long id, Category categoryUpdate) {
        Optional<Category> existingCategory = categoryDao.findById(id);
        if (existingCategory.isPresent()) {
            categoryUpdate.setId(id);
            return categoryDao.save(categoryUpdate);
        }
        return null;
    }
}
