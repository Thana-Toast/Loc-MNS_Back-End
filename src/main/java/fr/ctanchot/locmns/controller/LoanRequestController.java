package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.LoanRequestDTO;
import fr.ctanchot.locmns.dto.LoanRequestMapper;
import fr.ctanchot.locmns.model.LoanRequest;
import fr.ctanchot.locmns.service.LoanRequestService;
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
@RequestMapping("/loan-request")
@Tag(name = "4. Demandes de Prêt", description = "Gestion des demandes de prêt d'équipements (CRUD & Validation)")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;
    private final LoanRequestMapper loanRequestMapper;

    @Autowired
    public LoanRequestController(LoanRequestService loanRequestService, LoanRequestMapper loanRequestMapper) {
        this.loanRequestService = loanRequestService;
        this.loanRequestMapper = loanRequestMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des demandes de prêt", description = "Retourne la liste de toutes les demandes d'emprunt enregistrées.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Authentification requise")
    public List<LoanRequestDTO> getAll() {
        return loanRequestService.findAll().stream()
                .map(loanRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une demande de prêt par son ID", description = "Retourne le détail d'une demande d'emprunt spécifique en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Demande trouvée et renvoyée")
    @ApiResponse(responseCode = "404", description = "Demande introuvable")
    public ResponseEntity<LoanRequestDTO> get(@PathVariable Long id) {
        Optional<LoanRequest> loanRequest = loanRequestService.findById(id);
        return loanRequest.map(value -> new ResponseEntity<>(loanRequestMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer une demande de prêt", description = "Enregistre une nouvelle demande d'emprunt. Valide automatiquement si l'utilisateur possède un rôle ayant accès à la catégorie de l'équipement.")
    @ApiResponse(responseCode = "201", description = "Demande créée avec succès")
    @ApiResponse(responseCode = "400", description = "Format de requête invalide ou validation de droits de catégorie échouée")
    public ResponseEntity<LoanRequestDTO> create(
            @RequestBody
            @Valid
            LoanRequestDTO loanRequestDto
    ) {
        LoanRequest requestToInsert = loanRequestMapper.toEntity(loanRequestDto);
        LoanRequest savedRequest = loanRequestService.save(requestToInsert);
        return new ResponseEntity<>(loanRequestMapper.toDto(savedRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une demande de prêt", description = "Met à jour une demande existante (description, équipement ou utilisateur associé) tout en re-validant les droits d'accès.")
    @ApiResponse(responseCode = "200", description = "Demande mise à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Demande introuvable")
    public ResponseEntity<LoanRequestDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            LoanRequestDTO loanRequestDto
    ) {
        LoanRequest updatedRequest = loanRequestService.update(id, loanRequestDto);
        if (updatedRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanRequestMapper.toDto(updatedRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une demande de prêt", description = "Supprime définitivement une demande d'emprunt de la base de données.")
    @ApiResponse(responseCode = "204", description = "Demande supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Demande introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<LoanRequest> loanRequest = loanRequestService.findById(id);
        if (loanRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loanRequestService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

