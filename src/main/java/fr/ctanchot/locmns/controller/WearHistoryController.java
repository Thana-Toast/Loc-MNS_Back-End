package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.WearHistory;
import fr.ctanchot.locmns.security.IsAdmin;
import fr.ctanchot.locmns.service.WearHistoryService;
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
@IsAdmin
@RequestMapping("/wear-history")
@Tag(name = "10c. Historique - Usure", description = "Suivi de l'usure, dégradation et état physique du matériel - Réservé aux Administrateurs")
public class WearHistoryController {

    private final WearHistoryService wearHistoryService;

    public WearHistoryController(WearHistoryService wearHistoryService) {
        this.wearHistoryService = wearHistoryService;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer l'historique d'usure", description = "Retourne tout l'historique d'usure des équipements.")
    @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès")
    public List<WearHistory> getAll() {
        return wearHistoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une entrée d'historique d'usure par son ID", description = "Retourne les détails d'une entrée d'historique d'usure spécifique.")
    @ApiResponse(responseCode = "200", description = "Entrée trouvée et renvoyée")
    @ApiResponse(responseCode = "404", description = "Entrée introuvable")
    public ResponseEntity<WearHistory> get(@PathVariable Long id) {
        Optional<WearHistory> history = wearHistoryService.findById(id);
        return history.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer une entrée d'historique d'usure", description = "Enregistre une nouvelle entrée dans l'historique d'usure d'un équipement.")
    @ApiResponse(responseCode = "201", description = "Entrée d'historique créée")
    @ApiResponse(responseCode = "400", description = "Données d'entrée invalides")
    public ResponseEntity<WearHistory> create(
            @RequestBody
            @Valid
            WearHistory history
    ) {
        WearHistory savedHistory = wearHistoryService.save(history);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une entrée d'historique d'usure", description = "Supprime définitivement une entrée de l'historique d'usure.")
    @ApiResponse(responseCode = "204", description = "Entrée d'historique supprimée")
    @ApiResponse(responseCode = "404", description = "Entrée introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<WearHistory> history = wearHistoryService.findById(id);
        if (history.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        wearHistoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

