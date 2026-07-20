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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelService {

    private final ModelDao modelDao;
    private final CategoryDao categoryDao;
    private final FileStorageService fileStorageService;

    @Autowired
    public ModelService(ModelDao modelDao, CategoryDao categoryDao, FileStorageService fileStorageService) {
        this.modelDao = modelDao;
        this.categoryDao = categoryDao;
        this.fileStorageService = fileStorageService;
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

    @Transactional
    public void deleteById(Long id) {
        Optional<Model> existingModel = modelDao.findById(id);
        if (existingModel.isPresent()) {
            Model model = existingModel.get();
            String imagePath = model.getImagePath();

            // 1. Supprime en BDD d'abord et déclenche le flush immédiat
            modelDao.delete(model);
            modelDao.flush();

            // 2. Si la suppression SQL a réussi sans exception, on supprime l'image du disque
            if (imagePath != null && !imagePath.isBlank()) {
                fileStorageService.deleteFile(imagePath);
            }
        }
    }

    @Transactional
    public Model update(Long id, ModelDTO modelUpdate) {
        Optional<Model> existingModel = modelDao.findById(id);
        if (existingModel.isPresent()) {
            Model modelToUpdate = existingModel.get();
            
            String oldImagePath = modelToUpdate.getImagePath();
            String newImagePath = modelUpdate.getImagePath();

            String oldFileName = extractFileName(oldImagePath);
            String newFileName = extractFileName(newImagePath);

            // Supprime l'ancienne image uniquement si le NOM de fichier a réellement changé
            if (oldFileName != null && (newFileName == null || !oldFileName.equals(newFileName))) {
                fileStorageService.deleteFile(oldImagePath);
            }

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

    private String extractFileName(String path) {
        if (path == null || path.isBlank()) return null;
        int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    }
}
