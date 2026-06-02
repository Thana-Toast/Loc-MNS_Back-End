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
@Schema(description = "Représente un document technique ou un guide utilisateur lié à un modèle ou un matériel")
public class DocumentDTO {

    @Schema(description = "Identifiant unique du document", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Schema(description = "Titre explicite du document", example = "Notice d'utilisation du Dell Latitude 5420")
    private String title;

    @NotBlank(message = "Le chemin du fichier est obligatoire")
    @Schema(description = "Chemin d'accès ou URL du fichier (ex: PDF ou vidéo)", example = "/docs/manuals/dell_latitude_5420_manual.pdf")
    private String filePath;

    @NotNull(message = "Le type de document est obligatoire")
    @Schema(description = "Identifiant du type de document associé (ex: notice, vidéo de démo)", example = "2")
    private Long docTypeId;

    @Schema(description = "Libellé du type de document associé", example = "Notice technique", accessMode = Schema.AccessMode.READ_ONLY)
    private String docTypeLabel;

    @Schema(description = "Identifiant du modèle d'équipement auquel le document est rattaché (le cas échéant)", example = "1")
    private Long modelId;

    @Schema(description = "Nom/Libellé du modèle d'équipement rattaché", example = "Latitude 5420", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelLabel;

    @Schema(description = "Identifiant du matériel physique spécifique auquel le document est rattaché (le cas échéant)", example = "14")
    private Long equipmentId;

    @Schema(description = "Numéro de série du matériel physique rattaché", example = "SN-DELL-5420-9988", accessMode = Schema.AccessMode.READ_ONLY)
    private String equipmentSerialNumber;
}

