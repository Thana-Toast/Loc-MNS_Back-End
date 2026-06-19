package fr.ctanchot.locmns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @NotNull(message = "La date de début souhaitée est obligatoire")
    @Schema(description = "Date de début de prêt souhaitée", example = "2026-06-01")
    private LocalDate startDate;

    @NotNull(message = "La date de fin souhaitée est obligatoire")
    @Schema(description = "Date de fin de prêt souhaitée", example = "2026-06-15")
    private LocalDate endDate;

    @Schema(description = "Identifiant de l'utilisateur qui formule la demande de prêt", example = "2")
    private Long appUserId;

    @Schema(description = "Nom complet du demandeur", example = "Jean Dupont", accessMode = Schema.AccessMode.READ_ONLY)
    private String appUserFullName;

    @Schema(description = "Identifiant de l'équipement demandé pour le prêt (facultatif si modelId est fourni)", example = "10")
    private Long equipmentId;

    @Schema(description = "Numéro de série de l'équipement demandé", example = "SN-DELL-5420-12345", accessMode = Schema.AccessMode.READ_ONLY)
    private String equipmentSerialNumber;

    @Schema(description = "Identifiant du modèle d'équipement demandé pour le prêt (utilisé si equipmentId n'est pas fourni)", example = "1")
    private Long modelId;

    @Schema(description = "Nom du modèle d'équipement demandé", example = "Dell Pro 16", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelLabel;

    @Schema(description = "Statut actuel de la demande (PENDING, VALIDATED, REJECTED)", example = "PENDING", accessMode = Schema.AccessMode.READ_ONLY)
    private String status;

    @Schema(description = "Date effective de début de prêt", example = "2026-06-02", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate loanStartDate;

    @Schema(description = "Identifiant du prêt associé", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long loanId;

    @Schema(description = "Date de fin de prêt prévue", example = "2026-06-16", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate loanEndDate;

    @Schema(description = "Date réelle de retour", example = "2026-06-14", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate loanActualReturnDate;

    @Schema(description = "Indique si la demande est approuvée", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isApproved;

    @Schema(description = "Motif de refus de la demande", example = "Matériel indisponible", accessMode = Schema.AccessMode.READ_ONLY)
    private String rejectionReason;

    @Schema(description = "Rôles du demandeur", example = "[\"ROLE_USER\"]", accessMode = Schema.AccessMode.READ_ONLY)
    private java.util.List<String> appUserRoles;

    @Schema(description = "Email de contact du demandeur", example = "kirk.h@gmail.com", accessMode = Schema.AccessMode.READ_ONLY)
    private String appUserEmail;

    @Schema(description = "Marque du modèle demandé", example = "Dell", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelBrand;

    @Schema(description = "Description du modèle demandé", example = "Ordinateur...", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelDescription;

    @Schema(description = "Specs techniques du modèle demandé", example = "Intel...", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelTechnicalSpecs;

    @Schema(description = "Image du modèle demandé", example = "/images/dell.png", accessMode = Schema.AccessMode.READ_ONLY)
    private String modelImagePath;
}

