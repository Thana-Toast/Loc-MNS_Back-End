package fr.ctanchot.locmns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {

    private Long id;

    @NotBlank(message = "Le numéro de série est obligatoire")
    private String serialNumber;

    @NotNull(message = "Le modèle est obligatoire")
    private Long modelId;

    private String modelLabel;
}
