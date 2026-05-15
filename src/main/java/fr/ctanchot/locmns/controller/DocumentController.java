package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.DocumentDTO;
import fr.ctanchot.locmns.dto.DocumentMapper;
import fr.ctanchot.locmns.model.Document;
import fr.ctanchot.locmns.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Autowired
    public DocumentController(DocumentService documentService, DocumentMapper documentMapper) {
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @GetMapping("/list")
    public List<DocumentDTO> getAll() {
        return documentService.findAll().stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> get(@PathVariable Long id) {
        Optional<Document> document = documentService.findById(id);
        return document.map(value -> new ResponseEntity<>(documentMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> create(
            @RequestBody
            @Valid
            DocumentDTO documentDto
    ) {
        Document documentToInsert = documentMapper.toEntity(documentDto);
        Document savedDocument = documentService.save(documentToInsert);
        return new ResponseEntity<>(documentMapper.toDto(savedDocument), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            DocumentDTO documentDto
    ) {
        Document updatedDocument = documentService.update(id, documentDto);
        if (updatedDocument == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(documentMapper.toDto(updatedDocument), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Document> document = documentService.findById(id);
        if (document.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        documentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
