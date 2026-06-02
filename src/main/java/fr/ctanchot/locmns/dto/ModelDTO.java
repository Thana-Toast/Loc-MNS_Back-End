package fr.ctanchot.locmns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un modèle commercial de matériel informatique (ex: Latitude 5420)")
public class ModelDTO {

    @Schema(description = "Identifiant unique du modèle", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Schema(description = "Désignation commerciale / Libellé du modèle", example = "Latitude 5420")
    private String label;

    @NotBlank(message = "La marque est obligatoire")
    @Schema(description = "Marque ou constructeur du modèle", example = "Dell")
    private String brand;

    @NotBlank(message = "La description est obligatoire")
    @Schema(description = "Description commerciale ou présentation générale du modèle", example = "Ordinateur de bureau performant pour développeurs et créatifs.")
    private String description;

    @NotBlank(message = "Les specs techniques sont obligatoires")
    @Schema(description = "Caractéristiques techniques détaillées (CPU, RAM, Stockage, OS)", example = "Intel Core i7, 16Go RAM, SSD 512Go, Windows 11 Pro")
    private String technicalSpecs;

    @Schema(description = "Chemin d'accès ou URL vers l'image illustrative du modèle", example = "/images/models/dell_latitude_5420.png")
    private String imagePath;

    @NotNull(message = "La catégorie est obligatoire")
    @Schema(description = "Identifiant de la catégorie à laquelle ce modèle est associé", example = "3")
    private Long categoryId;

    @Schema(description = "Libellé de la catégorie associée", example = "Ordinateurs Portables", accessMode = Schema.AccessMode.READ_ONLY)
    private String categoryLabel;
}

