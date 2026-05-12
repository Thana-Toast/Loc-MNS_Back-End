package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {

    private final ModelDao modelDao;

    @Autowired
    public ModelService(ModelDao modelDao) {
        this.modelDao = modelDao;
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

    public Model update(Long id, Model modelUpdate) {
        Optional<Model> existingModel = modelDao.findById(id);
        if (existingModel.isPresent()) {
            modelUpdate.setId(id);
            return modelDao.save(modelUpdate);
        }
        return null;
    }
}
