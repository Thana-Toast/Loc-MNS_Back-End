package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.AvailabilityHistory;
import fr.ctanchot.locmns.service.AvailabilityHistoryService;
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
public class AvailabilityHistoryController {

    private final AvailabilityHistoryService availabilityHistoryService;

    @Autowired
    public AvailabilityHistoryController(AvailabilityHistoryService availabilityHistoryService) {
        this.availabilityHistoryService = availabilityHistoryService;
    }

    @GetMapping("/list")
    public List<AvailabilityHistory> getAll() {
        return availabilityHistoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityHistory> get(@PathVariable Long id) {
        Optional<AvailabilityHistory> history = availabilityHistoryService.findById(id);
        return history.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AvailabilityHistory> create(
            @RequestBody
            @Valid
            AvailabilityHistory history
    ) {
        AvailabilityHistory savedHistory = availabilityHistoryService.save(history);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<AvailabilityHistory> history = availabilityHistoryService.findById(id);
        if (history.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        availabilityHistoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
