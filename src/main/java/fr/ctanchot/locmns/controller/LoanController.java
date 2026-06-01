package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.LoanDTO;
import fr.ctanchot.locmns.dto.LoanMapper;
import fr.ctanchot.locmns.model.Loan;
import fr.ctanchot.locmns.service.LoanService;
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
@RequestMapping("/loan")
@Tag(name = "5. Prêts", description = "Gestion des prêts d'équipements (CRUD & Disponibilité)")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des prêts", description = "Retourne la liste de tous les prêts enregistrés au format DTO.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Authentification obligatoire")
    public List<LoanDTO> getAll() {
        return loanService.findAll().stream()
                .map(loanMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un prêt par son ID", description = "Retourne les détails d'un prêt d'équipement spécifique en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Prêt trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Prêt introuvable")
    public ResponseEntity<LoanDTO> get(@PathVariable Long id) {
        Optional<Loan> loan = loanService.findById(id);
        return loan.map(value -> new ResponseEntity<>(loanMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Enregistrer un nouveau prêt", description = "Crée un prêt d'équipement et vérifie au préalable que l'équipement n'est pas déjà réservé sur la même période.")
    @ApiResponse(responseCode = "201", description = "Prêt enregistré avec succès")
    @ApiResponse(responseCode = "400", description = "Conflit de dates ou données de prêt invalides")
    public ResponseEntity<LoanDTO> create(
            @RequestBody
            @Valid
            LoanDTO loanDto
    ) {
        Loan loanToInsert = loanMapper.toEntity(loanDto);
        Loan savedLoan = loanService.save(loanToInsert);
        return new ResponseEntity<>(loanMapper.toDto(savedLoan), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un prêt", description = "Met à jour un prêt existant (dates ou date de retour effectif) tout en validant la disponibilité.")
    @ApiResponse(responseCode = "200", description = "Prêt mis à jour avec succès")
    @ApiResponse(responseCode = "400", description = "Conflit de dates ou données de prêt invalides")
    @ApiResponse(responseCode = "404", description = "Prêt introuvable")
    public ResponseEntity<LoanDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            LoanDTO loanDto
    ) {
        Loan updatedLoan = loanService.update(id, loanDto);
        if (updatedLoan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanMapper.toDto(updatedLoan), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un prêt", description = "Supprime définitivement un prêt de la base de données par son ID.")
    @ApiResponse(responseCode = "204", description = "Prêt supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Prêt introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Loan> loan = loanService.findById(id);
        if (loan.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loanService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

