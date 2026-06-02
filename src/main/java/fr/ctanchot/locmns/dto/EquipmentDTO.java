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
@Schema(description = "Représente un matériel informatique physique (une unité avec un numéro de série)")
public class EquipmentDTO {

    @Schema(description = "Identifiant unique de l'équipement", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le numéro de série est obligatoire")
    @Schema(description = "Numéro de série unique du matériel apposé par le constructeur", example = "SN-DELL-5420-12345")
    private String serialNumber;

    @NotNull(message = "Le modèle est obligatoire")
    @Schema(description = "Identifiant du modèle auquel appartient cet équipement physique", example = "1")
    private Long modelId;

    @Schema(description = "Nom/Libellé du modèle de l'équipement", example = "Latitude 5420", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelLabel;
}

