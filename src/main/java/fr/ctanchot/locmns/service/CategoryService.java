package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.CategoryDao;
import fr.ctanchot.locmns.dto.CategoryDTO;
import fr.ctanchot.locmns.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return categoryDao.findAll();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return categoryDao.findAll();
        }

        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return categoryDao.findAuthorizedCategories(userRoles);
    }

    public Optional<Category> findById(Long id) {
        Optional<Category> optionalCategory = categoryDao.findById(id);
        if (optionalCategory.isEmpty()) {
            return Optional.empty();
        }

        Category category = optionalCategory.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.of(category);
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return Optional.of(category);
        }

        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        boolean isAuthorized = category.getRolesWithAccess().stream()
                .anyMatch(role -> userRoles.contains(role.getLabel()));

        if (isAuthorized) {
            return Optional.of(category);
        }

        return Optional.empty();
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
