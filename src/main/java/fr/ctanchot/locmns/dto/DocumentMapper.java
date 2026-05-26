package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.DocType;
import fr.ctanchot.locmns.model.Document;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.model.Model;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentDTO toDto(Document entity) {
        if (entity == null) return null;

        DocumentDTO dto = new DocumentDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setFilePath(entity.getFilePath());

        if (entity.getDocType() != null) {
            dto.setDocTypeId(entity.getDocType().getId());
            dto.setDocTypeLabel(entity.getDocType().getLabel());
        }

        if (entity.getModel() != null) {
            dto.setModelId(entity.getModel().getId());
            dto.setModelLabel(entity.getModel().getLabel());
        }

        if (entity.getEquipment() != null) {
            dto.setEquipmentId(entity.getEquipment().getId());
            dto.setEquipmentSerialNumber(entity.getEquipment().getSerialNumber());
        }

        return dto;
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) return null;

        Document entity = new Document();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setFilePath(dto.getFilePath());

        if (dto.getDocTypeId() != null) {
            DocType docType = new DocType();
            docType.setId(dto.getDocTypeId());
            entity.setDocType(docType);
        }

        if (dto.getModelId() != null) {
            Model model = new Model();
            model.setId(dto.getModelId());
            entity.setModel(model);
        }

        if (dto.getEquipmentId() != null) {
            Equipment equipment = new Equipment();
            equipment.setId(dto.getEquipmentId());
            entity.setEquipment(equipment);
        }

        return entity;
    }
}

