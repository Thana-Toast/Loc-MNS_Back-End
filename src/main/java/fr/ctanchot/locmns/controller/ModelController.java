package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.Model;
import fr.ctanchot.locmns.service.ModelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/model")
public class ModelController {

    private final ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/list")
    public List<Model> getAll() {
        return modelService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> get(@PathVariable Long id) {
        Optional<Model> model = modelService.findById(id);
        return model.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Model> create(
            @RequestBody
            @Valid
            Model model
    ) {
        Model savedModel = modelService.save(model);
        return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            Model model
    ) {
        Model updatedModel = modelService.update(id, model);
        if (updatedModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Model> model = modelService.findById(id);
        if (model.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        modelService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
