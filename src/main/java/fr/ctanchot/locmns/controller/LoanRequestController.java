package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.LoanRequestDTO;
import fr.ctanchot.locmns.dto.LoanRequestMapper;
import fr.ctanchot.locmns.model.LoanRequest;
import fr.ctanchot.locmns.service.LoanRequestService;
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
public class LoanRequestController {

    private final LoanRequestService loanRequestService;
    private final LoanRequestMapper loanRequestMapper;

    @Autowired
    public LoanRequestController(LoanRequestService loanRequestService, LoanRequestMapper loanRequestMapper) {
        this.loanRequestService = loanRequestService;
        this.loanRequestMapper = loanRequestMapper;
    }

    @GetMapping("/list")
    public List<LoanRequestDTO> getAll() {
        return loanRequestService.findAll().stream()
                .map(loanRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequestDTO> get(@PathVariable Long id) {
        Optional<LoanRequest> loanRequest = loanRequestService.findById(id);
        return loanRequest.map(value -> new ResponseEntity<>(loanRequestMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<LoanRequest> loanRequest = loanRequestService.findById(id);
        if (loanRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loanRequestService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
