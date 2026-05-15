package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.EquipmentDTO;
import fr.ctanchot.locmns.dto.EquipmentMapper;
import fr.ctanchot.locmns.model.Equipment;
import fr.ctanchot.locmns.service.EquipmentService;
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
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @GetMapping("/list")
    public List<EquipmentDTO> getAll() {
        return equipmentService.findAll().stream()
                .map(equipmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> get(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        return equipment.map(value -> new ResponseEntity<>(equipmentMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> create(
            @RequestBody
            @Valid
            EquipmentDTO equipmentDto
    ) {
        Equipment equipmentToInsert = equipmentMapper.toEntity(equipmentDto);
        Equipment savedEquipment = equipmentService.save(equipmentToInsert);
        return new ResponseEntity<>(equipmentMapper.toDto(savedEquipment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            EquipmentDTO equipmentDto
    ) {
        Equipment updatedEquipment = equipmentService.update(id, equipmentDto);
        if (updatedEquipment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(equipmentMapper.toDto(updatedEquipment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        if (equipment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        equipmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
