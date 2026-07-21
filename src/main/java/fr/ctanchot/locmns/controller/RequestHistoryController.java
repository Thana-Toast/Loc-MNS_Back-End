package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.RequestHistory;
import fr.ctanchot.locmns.security.IsAdmin;
import fr.ctanchot.locmns.service.RequestHistoryService;
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
@RequestMapping("/request-history")
@Tag(name = "10b. Historique - Demandes", description = "Suivi historique des demandes de prêt - Réservé aux Administrateurs")
public class RequestHistoryController {

    private final RequestHistoryService requestHistoryService;

    public RequestHistoryController(RequestHistoryService requestHistoryService) {
        this.requestHistoryService = requestHistoryService;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer l'historique des demandes", description = "Retourne tout l'historique des demandes de prêt.")
    @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès")
    public List<RequestHistory> getAll() {
        return requestHistoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une entrée d'historique de demande par son ID", description = "Retourne les détails d'un historique de demande spécifique.")
    @ApiResponse(responseCode = "200", description = "Entrée trouvée et renvoyée")
    @ApiResponse(responseCode = "404", description = "Entrée introuvable")
    public ResponseEntity<RequestHistory> get(@PathVariable Long id) {
        Optional<RequestHistory> history = requestHistoryService.findById(id);
        return history.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer une entrée d'historique de demande", description = "Enregistre une nouvelle entrée dans l'historique des demandes.")
    @ApiResponse(responseCode = "201", description = "Entrée d'historique créée")
    @ApiResponse(responseCode = "400", description = "Données d'entrée invalides")
    public ResponseEntity<RequestHistory> create(
            @RequestBody
            @Valid
            RequestHistory history
    ) {
        RequestHistory savedHistory = requestHistoryService.save(history);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une entrée d'historique de demande", description = "Supprime définitivement une entrée de l'historique des demandes.")
    @ApiResponse(responseCode = "204", description = "Entrée d'historique supprimée")
    @ApiResponse(responseCode = "404", description = "Entrée introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<RequestHistory> history = requestHistoryService.findById(id);
        if (history.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        requestHistoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

