package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.TicketDTO;
import fr.ctanchot.locmns.dto.TicketMapper;
import fr.ctanchot.locmns.model.Ticket;
import fr.ctanchot.locmns.model.TicketStatus;
import fr.ctanchot.locmns.service.TicketService;
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
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Autowired
    public TicketController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/list")
    public List<TicketDTO> getAll() {
        return ticketService.findAll().stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> get(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        return ticket.map(value -> new ResponseEntity<>(ticketMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        if (ticket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ticketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        ticketService.changeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
