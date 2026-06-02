package fr.ctanchot.locmns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente une catégorie d'équipements (ex: Ordinateurs, Écrans, Accessoires)")
public class CategoryDTO {

    @Schema(description = "Identifiant unique de la catégorie", example = "3", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Schema(description = "Libellé de la catégorie", example = "Ordinateurs Portables")
    private String label;

    @NotBlank(message = "La description est obligatoire")
    @Schema(description = "Description détaillée du type d'équipement associé à cette catégorie", example = "Tous les ordinateurs portables de l'entreprise (Dell, MacBook, etc.)")
    private String description;

    @Schema(description = "Identifiant de la catégorie parente (si applicable, pour gérer des sous-catégories)", example = "1")
    private Long parentCategoryId;

    @Schema(description = "Libellé de la catégorie parente", example = "Informatique", accessMode = Schema.AccessMode.READ_ONLY)
    private String parentCategoryLabel;
}

