package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.DocumentDao;
import fr.ctanchot.locmns.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentDao documentDao;

    @Autowired
    public DocumentService(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    public List<Document> findAll() {
        return documentDao.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentDao.findById(id);
    }

    public Document save(Document document) {
        return documentDao.save(document);
    }

    public void deleteById(Long id) {
        documentDao.deleteById(id);
    }

    public Document update(Long id, Document documentUpdate) {
        Optional<Document> existing = documentDao.findById(id);
        if (existing.isPresent()) {
            documentUpdate.setId(id);
            return documentDao.save(documentUpdate);
        }
        return null;
    }
}
