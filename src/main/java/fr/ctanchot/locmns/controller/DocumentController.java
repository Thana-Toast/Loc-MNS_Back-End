package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.DocumentDTO;
import fr.ctanchot.locmns.dto.DocumentMapper;
import fr.ctanchot.locmns.model.Document;
import fr.ctanchot.locmns.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "09. Documents", description = "Gestion des notices techniques, guides et supports vidéos (CRUD)")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    public DocumentController(DocumentService documentService, DocumentMapper documentMapper) {
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des documents", description = "Retourne la liste de tous les documents enregistrés (notices techniques, vidéos).")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public List<DocumentDTO> getAll() {
        return documentService.findAll().stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un document par son ID", description = "Retourne les détails d'un document spécifique en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Document trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Document introuvable")
    public ResponseEntity<DocumentDTO> get(@PathVariable Long id) {
        Optional<Document> document = documentService.findById(id);
        return document.map(value -> new ResponseEntity<>(documentMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau document", description = "Enregistre un nouveau document lié à un modèle de matériel ou à un équipement spécifique.")
    @ApiResponse(responseCode = "201", description = "Document créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données de document invalides")
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
    @Operation(summary = "Mettre à jour un document", description = "Met à jour le titre, le chemin de fichier ou les associations d'un document existant.")
    @ApiResponse(responseCode = "200", description = "Document mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Document introuvable")
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
    @Operation(summary = "Supprimer un document", description = "Supprime définitivement un document de la base de données par son ID.")
    @ApiResponse(responseCode = "204", description = "Document supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Document introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Document> document = documentService.findById(id);
        if (document.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        documentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

