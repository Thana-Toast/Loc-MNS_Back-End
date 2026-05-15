package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.LoanDTO;
import fr.ctanchot.locmns.dto.LoanMapper;
import fr.ctanchot.locmns.model.Loan;
import fr.ctanchot.locmns.service.LoanService;
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
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @Autowired
    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @GetMapping("/list")
    public List<LoanDTO> getAll() {
        return loanService.findAll().stream()
                .map(loanMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> get(@PathVariable Long id) {
        Optional<Loan> loan = loanService.findById(id);
        return loan.map(value -> new ResponseEntity<>(loanMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Loan> loan = loanService.findById(id);
        if (loan.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loanService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
