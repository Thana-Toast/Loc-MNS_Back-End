package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.CategoryDao;
import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.dto.ModelDTO;
import fr.ctanchot.locmns.model.Category;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelService {

    private final ModelDao modelDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ModelService(ModelDao modelDao, CategoryDao categoryDao) {
        this.modelDao = modelDao;
        this.categoryDao = categoryDao;
    }

    public List<Model> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return modelDao.findAll();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return modelDao.findAll();
        }

        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return modelDao.findModelsByAuthorizedCategories(userRoles);
    }

    public Optional<Model> findById(Long id) {
        Optional<Model> optionalModel = modelDao.findById(id);
        if (optionalModel.isEmpty()) {
            return Optional.empty();
        }

        Model model = optionalModel.get();
        Category category = model.getCategory();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || category == null) {
            return Optional.of(model);
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return Optional.of(model);
        }

        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        boolean isAuthorized = category.getRolesWithAccess().stream()
                .anyMatch(role -> userRoles.contains(role.getLabel()));

        if (isAuthorized) {
            return Optional.of(model);
        }

        return Optional.empty();
    }

    public Model save(Model model) {
        return modelDao.save(model);
    }

    public void deleteById(Long id) {
        modelDao.deleteById(id);
    }

    public Model update(Long id, ModelDTO modelUpdate) {
        Optional<Model> existingModel = modelDao.findById(id);
        if (existingModel.isPresent()) {
            Model modelToUpdate = existingModel.get();
            
            modelToUpdate.setLabel(modelUpdate.getLabel());
            modelToUpdate.setBrand(modelUpdate.getBrand());
            modelToUpdate.setDescription(modelUpdate.getDescription());
            modelToUpdate.setTechnicalSpecs(modelUpdate.getTechnicalSpecs());
            modelToUpdate.setImagePath(modelUpdate.getImagePath());

            if (modelUpdate.getCategoryId() != null) {
                Optional<Category> category = categoryDao.findById(modelUpdate.getCategoryId());
                category.ifPresent(modelToUpdate::setCategory);
            }
            
            return modelDao.save(modelToUpdate);
        }
        return null;
    }
}
