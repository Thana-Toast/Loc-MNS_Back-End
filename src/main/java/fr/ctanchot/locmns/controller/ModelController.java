package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.ModelDTO;
import fr.ctanchot.locmns.dto.ModelMapper;
import fr.ctanchot.locmns.model.Model;
import fr.ctanchot.locmns.service.ModelService;
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
@RequestMapping("/model")
public class ModelController {

    private final ModelService modelService;
    private final ModelMapper modelMapper;

    @Autowired
    public ModelController(ModelService modelService, ModelMapper modelMapper) {
        this.modelService = modelService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public List<ModelDTO> getAll() {
        return modelService.findAll().stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelDTO> get(@PathVariable Long id) {
        Optional<Model> model = modelService.findById(id);
        return model.map(value -> new ResponseEntity<>(modelMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ModelDTO> create(
            @RequestBody
            @Valid
            ModelDTO modelDto
    ) {
        Model modelToInsert = modelMapper.toEntity(modelDto);
        Model savedModel = modelService.save(modelToInsert);
        return new ResponseEntity<>(modelMapper.toDto(savedModel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            ModelDTO modelDto
    ) {
        Model updatedModel = modelService.update(id, modelDto);
        if (updatedModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modelMapper.toDto(updatedModel), HttpStatus.OK);
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
