package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.DocType;
import fr.ctanchot.locmns.service.DocTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/doc-type")
public class DocTypeController {

    private final DocTypeService docTypeService;

    @Autowired
    public DocTypeController(DocTypeService docTypeService) {
        this.docTypeService = docTypeService;
    }

    @GetMapping("/list")
    public List<DocType> getAll() {
        return docTypeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocType> get(@PathVariable Long id) {
        Optional<DocType> docType = docTypeService.findById(id);
        return docType.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<DocType> create(@RequestBody DocType docType) {
        DocType savedDocType = docTypeService.save(docType);
        return new ResponseEntity<>(savedDocType, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<DocType> docType = docTypeService.findById(id);
        if (docType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        docTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
