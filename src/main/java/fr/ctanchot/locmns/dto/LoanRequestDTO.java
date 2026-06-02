package fr.ctanchot.locmns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente une demande de prêt de matériel soumise par un utilisateur")
public class LoanRequestDTO {

    @Schema(description = "Identifiant unique de la demande de prêt", example = "4", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Date et heure de soumission de la demande", example = "2026-06-01T09:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime date;

    @NotBlank(message = "La description est obligatoire")
    @Schema(description = "Description du besoin ou motif justifiant la demande de prêt", example = "Besoin d'un ordinateur portable Dell pour un déplacement professionnel de 2 semaines.")
    private String description;

    @NotNull(message = "L'utilisateur est obligatoire")
    @Schema(description = "Identifiant de l'utilisateur qui formule la demande de prêt", example = "2")
    private Long appUserId;

    @Schema(description = "Nom complet du demandeur", example = "Jean Dupont", accessMode = Schema.AccessMode.READ_ONLY)
    private String appUserFullName;

    @NotNull(message = "L'équipement est obligatoire")
    @Schema(description = "Identifiant de l'équipement demandé pour le prêt", example = "10")
    private Long equipmentId;

    @Schema(description = "Numéro de série de l'équipement demandé", example = "SN-DELL-5420-12345", accessMode = Schema.AccessMode.READ_ONLY)
    private String equipmentSerialNumber;
}

