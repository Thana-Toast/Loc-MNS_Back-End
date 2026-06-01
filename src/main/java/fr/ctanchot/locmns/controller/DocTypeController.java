package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.DocType;
import fr.ctanchot.locmns.service.DocTypeService;
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

@RestController
@CrossOrigin
@RequestMapping("/doc-type")
@Tag(name = "9. Types de Documents", description = "Gestion des catégories ou types de fichiers (CRUD)")
public class DocTypeController {

    private final DocTypeService docTypeService;

    @Autowired
    public DocTypeController(DocTypeService docTypeService) {
        this.docTypeService = docTypeService;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des types de documents", description = "Retourne tous les types de fichiers (ex: Notice technique, Guide d'utilisation, Vidéo de démonstration).")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public List<DocType> getAll() {
        return docTypeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un type de document par son ID", description = "Retourne les détails d'un type de document en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Type trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Type introuvable")
    public ResponseEntity<DocType> get(@PathVariable Long id) {
        Optional<DocType> docType = docTypeService.findById(id);
        return docType.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer un type de document", description = "Enregistre un nouveau type de document (ex: Notice PDF).")
    @ApiResponse(responseCode = "201", description = "Type créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données de type invalides")
    public ResponseEntity<DocType> create(
            @RequestBody
            @Valid
            DocType docType
    ) {
        DocType savedDocType = docTypeService.save(docType);
        return new ResponseEntity<>(savedDocType, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un type de document", description = "Supprime définitivement un type de document de la base de données par son ID.")
    @ApiResponse(responseCode = "204", description = "Type supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Type introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<DocType> docType = docTypeService.findById(id);
        if (docType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        docTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

