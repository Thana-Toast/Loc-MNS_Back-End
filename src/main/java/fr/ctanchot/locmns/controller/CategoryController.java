package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.CategoryDTO;
import fr.ctanchot.locmns.dto.CategoryMapper;
import fr.ctanchot.locmns.model.Category;
import fr.ctanchot.locmns.service.CategoryService;
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
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/list")
    public List<CategoryDTO> getAll() {
        return categoryService.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> get(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(value -> new ResponseEntity<>(categoryMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(
            @RequestBody
            @Valid
            CategoryDTO categoryDto
    ) {
        Category categoryToInsert = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryService.save(categoryToInsert);
        return new ResponseEntity<>(categoryMapper.toDto(savedCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            CategoryDTO categoryDto
    ) {
        Category updatedCategory = categoryService.update(id, categoryDto);
        if (updatedCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoryMapper.toDto(updatedCategory), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
