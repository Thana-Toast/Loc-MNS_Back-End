package fr.ctanchot.locmns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "Le chemin du fichier est obligatoire")
    private String filePath;

    @NotNull(message = "Le type de document est obligatoire")
    private Long docTypeId;

    private String docTypeLabel; // Pour l'affichage Front

    private Long modelId;

    private String modelLabel; // Pour l'affichage Front

    private Long equipmentId;

    private String equipmentSerialNumber; // Pour l'affichage Front
}
