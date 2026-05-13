package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.WearHistory;
import fr.ctanchot.locmns.service.WearHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/wear-history")
public class WearHistoryController {

    private final WearHistoryService wearHistoryService;

    @Autowired
    public WearHistoryController(WearHistoryService wearHistoryService) {
        this.wearHistoryService = wearHistoryService;
    }

    @GetMapping("/list")
    public List<WearHistory> getAll() {
        return wearHistoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WearHistory> get(@PathVariable Long id) {
        Optional<WearHistory> history = wearHistoryService.findById(id);
        return history.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<WearHistory> create(
            @RequestBody
            @Valid
            WearHistory history
    ) {
        WearHistory savedHistory = wearHistoryService.save(history);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<WearHistory> history = wearHistoryService.findById(id);
        if (history.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        wearHistoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
