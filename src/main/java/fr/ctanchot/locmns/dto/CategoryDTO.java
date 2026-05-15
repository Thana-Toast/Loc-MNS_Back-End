package fr.ctanchot.locmns.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Le libellé est obligatoire")
    private String label;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    private Long parentCategoryId;

    private String parentCategoryLabel;
}
