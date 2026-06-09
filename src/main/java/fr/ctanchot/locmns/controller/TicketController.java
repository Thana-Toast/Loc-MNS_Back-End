package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.TicketDTO;
import fr.ctanchot.locmns.dto.TicketMapper;
import fr.ctanchot.locmns.model.Ticket;
import fr.ctanchot.locmns.model.TicketStatus;
import fr.ctanchot.locmns.service.TicketService;
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
@RequestMapping("/ticket")
@Tag(name = "08. Incidents (Tickets)", description = "Signalement des pannes et dysfonctionnements (CRUD & Statut)")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public TicketController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des tickets d'incident", description = "Retourne la liste de tous les tickets d'incident signalés sur le parc.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Authentification obligatoire")
    public List<TicketDTO> getAll() {
        return ticketService.findAll().stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un ticket par son ID", description = "Retourne les détails d'un ticket spécifique et son statut actuel issu de son historique.")
    @ApiResponse(responseCode = "200", description = "Ticket trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Ticket introuvable")
    public ResponseEntity<TicketDTO> get(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        return ticket.map(value -> new ResponseEntity<>(ticketMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Signaler un nouvel incident (créer un ticket)", description = "Crée un nouveau ticket d'incident sur un équipement. Par défaut, le statut initial du ticket est défini sur PENDING.")
    @ApiResponse(responseCode = "201", description = "Ticket créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données de ticket invalides")
    public ResponseEntity<TicketDTO> create(
            @RequestBody
            @Valid
            TicketDTO ticketDto
    ) {
        Ticket ticketToInsert = ticketMapper.toEntity(ticketDto);
        Ticket savedTicket = ticketService.save(ticketToInsert);
        return new ResponseEntity<>(ticketMapper.toDto(savedTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un ticket d'incident", description = "Met à jour le contenu descriptif ou l'équipement lié à un ticket d'incident existant.")
    @ApiResponse(responseCode = "200", description = "Ticket mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Ticket introuvable")
    public ResponseEntity<TicketDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            TicketDTO ticketDto
    ) {
        Ticket updatedTicket = ticketService.update(id, ticketDto);
        if (updatedTicket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ticketMapper.toDto(updatedTicket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un ticket d'incident", description = "Supprime définitivement un ticket d'incident de la base de données par son ID.")
    @ApiResponse(responseCode = "204", description = "Ticket supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Ticket introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        if (ticket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ticketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'un ticket", description = "Ajoute une nouvelle entrée d'historique (ex: PENDING, RESOLVED, IN_PROGRESS) pour changer le statut actuel du ticket d'incident.")
    @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Ticket introuvable")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        ticketService.changeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

