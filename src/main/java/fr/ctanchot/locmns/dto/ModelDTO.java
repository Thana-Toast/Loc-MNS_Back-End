package fr.ctanchot.locmns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelDTO {

    private Long id;

    @NotBlank(message = "Le libellé est obligatoire")
    private String label;

    @NotBlank(message = "La marque est obligatoire")
    private String brand;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotBlank(message = "Les specs techniques sont obligatoires")
    private String technicalSpecs;

    private String imagePath;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    private String categoryLabel; // Pour l'affichage Front
}
