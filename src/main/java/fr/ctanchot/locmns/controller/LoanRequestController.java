package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.LoanRequest;
import fr.ctanchot.locmns.service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/loan-request")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    @Autowired
    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @GetMapping("/list")
    public List<LoanRequest> getAll() {
        return loanRequestService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequest> get(@PathVariable Long id) {
        Optional<LoanRequest> loanRequest = loanRequestService.findById(id);
        return loanRequest.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<LoanRequest> create(@RequestBody LoanRequest loanRequest) {
        LoanRequest savedRequest = loanRequestService.save(loanRequest);
        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanRequest> update(@PathVariable Long id, @RequestBody LoanRequest loanRequest) {
        LoanRequest updatedRequest = loanRequestService.update(id, loanRequest);
        if (updatedRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<LoanRequest> loanRequest = loanRequestService.findById(id);
        if (loanRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loanRequestService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
