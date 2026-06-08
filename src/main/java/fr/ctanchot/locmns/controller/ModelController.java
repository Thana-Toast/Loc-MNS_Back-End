package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.ModelDTO;
import fr.ctanchot.locmns.dto.ModelMapper;
import fr.ctanchot.locmns.model.Model;
import fr.ctanchot.locmns.service.ModelService;
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
@RequestMapping("/model")
@Tag(name = "08. Modèles", description = "Gestion des modèles d'équipements (CRUD)")
public class ModelController {

    private final ModelService modelService;
    private final ModelMapper modelMapper;

    public ModelController(ModelService modelService, ModelMapper modelMapper) {
        this.modelService = modelService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des modèles", description = "Retourne la liste de tous les modèles d'équipements (ex: MacBook Pro 16, Écran Dell 27).")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public List<ModelDTO> getAll() {
        return modelService.findAll().stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un modèle par son ID", description = "Retourne les détails d'un modèle d'équipement spécifique en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Modèle trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Modèle introuvable")
    public ResponseEntity<ModelDTO> get(@PathVariable Long id) {
        Optional<Model> model = modelService.findById(id);
        return model.map(value -> new ResponseEntity<>(modelMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau modèle", description = "Enregistre un nouveau modèle de matériel (marque, fiche technique, image, catégorie liée).")
    @ApiResponse(responseCode = "201", description = "Modèle créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données de modèle invalides")
    public ResponseEntity<ModelDTO> create(
            @RequestBody
            @Valid
            ModelDTO modelDto
    ) {
        Model modelToInsert = modelMapper.toEntity(modelDto);
        Model savedModel = modelService.save(modelToInsert);
        return new ResponseEntity<>(modelMapper.toDto(savedModel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un modèle", description = "Met à jour la marque, la fiche technique ou la catégorie d'un modèle existant.")
    @ApiResponse(responseCode = "200", description = "Modèle mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Modèle introuvable")
    public ResponseEntity<ModelDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            ModelDTO modelDto
    ) {
        Model updatedModel = modelService.update(id, modelDto);
        if (updatedModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modelMapper.toDto(updatedModel), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un modèle", description = "Supprime définitivement un modèle de matériel de la base de données par son ID.")
    @ApiResponse(responseCode = "204", description = "Modèle supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Modèle introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Model> model = modelService.findById(id);
        if (model.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        modelService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

