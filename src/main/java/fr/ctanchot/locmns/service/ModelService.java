package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.CategoryDao;
import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.dto.ModelDTO;
import fr.ctanchot.locmns.model.Category;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return modelDao.findAll();
    }

    public Optional<Model> findById(Long id) {
        return modelDao.findById(id);
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
