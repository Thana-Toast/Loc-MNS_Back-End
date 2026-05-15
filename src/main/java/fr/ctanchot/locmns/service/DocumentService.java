package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.DocTypeDao;
import fr.ctanchot.locmns.dao.DocumentDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.ModelDao;
import fr.ctanchot.locmns.dto.DocumentDTO;
import fr.ctanchot.locmns.model.DocType;
import fr.ctanchot.locmns.model.Document;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentDao documentDao;
    private final DocTypeDao docTypeDao;
    private final ModelDao modelDao;
    private final EquipmentDao equipmentDao;

    @Autowired
    public DocumentService(DocumentDao documentDao, DocTypeDao docTypeDao, ModelDao modelDao, EquipmentDao equipmentDao) {
        this.documentDao = documentDao;
        this.docTypeDao = docTypeDao;
        this.modelDao = modelDao;
        this.equipmentDao = equipmentDao;
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

    public Document update(Long id, DocumentDTO documentUpdate) {
        Optional<Document> existingOpt = documentDao.findById(id);
        if (existingOpt.isPresent()) {
            Document documentToUpdate = existingOpt.get();
            
            documentToUpdate.setTitle(documentUpdate.getTitle());
            documentToUpdate.setFilePath(documentUpdate.getFilePath());

            if (documentUpdate.getDocTypeId() != null) {
                Optional<DocType> docType = docTypeDao.findById(documentUpdate.getDocTypeId());
                docType.ifPresent(documentToUpdate::setDocType);
            }

            // Gestion du XOR (SOIT Modèle SOIT Equipement)
            if (documentUpdate.getModelId() != null) {
                Optional<Model> model = modelDao.findById(documentUpdate.getModelId());
                if (model.isPresent()) {
                    documentToUpdate.setModel(model.get());
                    documentToUpdate.setEquipment(null);
                }
            } else if (documentUpdate.getEquipmentId() != null) {
                Optional<Equipment> equipment = equipmentDao.findById(documentUpdate.getEquipmentId());
                if (equipment.isPresent()) {
                    documentToUpdate.setEquipment(equipment.get());
                    documentToUpdate.setModel(null);
                }
            }
            
            return documentDao.save(documentToUpdate);
        }
        return null;
    }
}
