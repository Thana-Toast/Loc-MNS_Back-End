package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.DocTypeDao;
import fr.ctanchot.locmns.model.DocType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocTypeService {

    private final DocTypeDao docTypeDao;

    @Autowired
    public DocTypeService(DocTypeDao docTypeDao) {
        this.docTypeDao = docTypeDao;
    }

    public List<DocType> findAll() {
        return docTypeDao.findAll();
    }

    public Optional<DocType> findById(Long id) {
        return docTypeDao.findById(id);
    }

    public DocType save(DocType docType) {
        return docTypeDao.save(docType);
    }

    public void deleteById(Long id) {
        docTypeDao.deleteById(id);
    }
}
