package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.AvailabilityHistory;
import fr.ctanchot.locmns.service.AvailabilityHistoryService;
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
@RequestMapping("/availability-history")
@Tag(name = "9b. Historique - Disponibilités", description = "Suivi de l'état de disponibilité du matériel (Disponible, Loué, En réparation, Hors service)")
public class AvailabilityHistoryController {

    private final AvailabilityHistoryService availabilityHistoryService;

    public AvailabilityHistoryController(AvailabilityHistoryService availabilityHistoryService) {
        this.availabilityHistoryService = availabilityHistoryService;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer l'historique des états de disponibilité", description = "Retourne tout l'historique des changements de statut de disponibilité des matériels.")
    @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès")
    public List<AvailabilityHistory> getAll() {
        return availabilityHistoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une entrée d'historique de disponibilité par son ID", description = "Retourne les détails d'un changement d'état spécifique.")
    @ApiResponse(responseCode = "200", description = "Entrée trouvée et renvoyée")
    @ApiResponse(responseCode = "404", description = "Entrée introuvable")
    public ResponseEntity<AvailabilityHistory> get(@PathVariable Long id) {
        Optional<AvailabilityHistory> history = availabilityHistoryService.findById(id);
        return history.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Ajouter une entrée d'historique de disponibilité", description = "Enregistre un changement d'état de disponibilité sur un équipement spécifique.")
    @ApiResponse(responseCode = "201", description = "Entrée d'historique créée")
    @ApiResponse(responseCode = "400", description = "Données d'entrée invalides")
    public ResponseEntity<AvailabilityHistory> create(
            @RequestBody
            @Valid
            AvailabilityHistory history
    ) {
        AvailabilityHistory savedHistory = availabilityHistoryService.save(history);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une entrée d'historique de disponibilité", description = "Supprime définitivement une entrée d'historique de la base de données.")
    @ApiResponse(responseCode = "204", description = "Entrée d'historique supprimée")
    @ApiResponse(responseCode = "404", description = "Entrée introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<AvailabilityHistory> history = availabilityHistoryService.findById(id);
        if (history.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        availabilityHistoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

