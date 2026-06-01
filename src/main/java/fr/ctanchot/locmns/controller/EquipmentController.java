package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.EquipmentDTO;
import fr.ctanchot.locmns.dto.EquipmentMapper;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.service.EquipmentService;
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
@RequestMapping("/equipment")
@Tag(name = "3. Équipements", description = "Gestion du matériel informatique physique (CRUD)")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des équipements", description = "Retourne la liste de tous les matériels du parc informatique au format DTO.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Authentification obligatoire")
    public List<EquipmentDTO> getAll() {
        return equipmentService.findAll().stream()
                .map(equipmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un équipement par son ID", description = "Retourne les détails d'un équipement informatique spécifique en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Équipement trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    public ResponseEntity<EquipmentDTO> get(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        return equipment.map(value -> new ResponseEntity<>(equipmentMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouvel équipement", description = "Crée un nouvel équipement physique dans le stock (ex: avec numéro de série).")
    @ApiResponse(responseCode = "201", description = "Équipement créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données d'équipement invalides")
    public ResponseEntity<EquipmentDTO> create(
            @RequestBody
            @Valid
            EquipmentDTO equipmentDto
    ) {
        Equipment equipmentToInsert = equipmentMapper.toEntity(equipmentDto);
        Equipment savedEquipment = equipmentService.save(equipmentToInsert);
        return new ResponseEntity<>(equipmentMapper.toDto(savedEquipment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un équipement", description = "Met à jour les informations d'un équipement existant (numéro de série, modèle lié).")
    @ApiResponse(responseCode = "200", description = "Équipement mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    public ResponseEntity<EquipmentDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            EquipmentDTO equipmentDto
    ) {
        Equipment updatedEquipment = equipmentService.update(id, equipmentDto);
        if (updatedEquipment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(equipmentMapper.toDto(updatedEquipment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un équipement", description = "Supprime définitivement un équipement du parc informatique par son ID.")
    @ApiResponse(responseCode = "204", description = "Équipement supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        if (equipment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        equipmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

