package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.CategoryDTO;
import fr.ctanchot.locmns.dto.CategoryMapper;
import fr.ctanchot.locmns.model.Category;
import fr.ctanchot.locmns.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "7. Catégories", description = "Gestion des catégories d'équipements (CRUD)")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des catégories", description = "Retourne la liste de toutes les catégories d'équipements (ex: Informatique, Mobilier).")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public List<CategoryDTO> getAll() {
        return categoryService.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par son ID", description = "Retourne les détails d'une catégorie d'équipements en fonction de son ID.")
    @ApiResponse(responseCode = "200", description = "Catégorie trouvée et renvoyée")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable")
    public ResponseEntity<CategoryDTO> get(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        return category.map(value -> new ResponseEntity<>(categoryMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle catégorie", description = "Enregistre une nouvelle catégorie de matériel.")
    @ApiResponse(responseCode = "201", description = "Catégorie créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données de catégorie invalides")
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
    @Operation(summary = "Mettre à jour une catégorie", description = "Met à jour le libellé, la description ou la catégorie parente d'une catégorie existante.")
    @ApiResponse(responseCode = "200", description = "Catégorie mise à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable")
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
    @Operation(summary = "Supprimer une catégorie", description = "Supprime définitivement une catégorie de matériel de la base de données.")
    @ApiResponse(responseCode = "204", description = "Catégorie supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Catégorie introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

