package fr.ctanchot.locmns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un prêt effectif de matériel informatique")
public class LoanDTO {

    @Schema(description = "Identifiant unique du prêt", example = "8", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "La date de début est obligatoire")
    @Schema(description = "Date effective de début du prêt", example = "2026-06-01")
    private LocalDate startDate;

    @NotNull(message = "La date de fin est obligatoire")
    @Schema(description = "Date de fin de prêt prévue (date limite de restitution)", example = "2026-06-15")
    private LocalDate endDate;

    @Schema(description = "Date réelle de restitution de l'équipement (renseignée au retour du matériel)", example = "2026-06-14")
    private LocalDate actualReturnDate;

    @NotNull(message = "La demande de prêt est obligatoire")
    @Schema(description = "Identifiant de la demande de prêt d'origine validée", example = "4")
    private Long loanRequestId;

    @Schema(description = "Numéro de série du matériel physique prêté", example = "SN-DELL-5420-12345", accessMode = Schema.AccessMode.READ_ONLY)
    private String equipmentSerialNumber;

    @Schema(description = "Nom complet de l'utilisateur bénéficiant du prêt", example = "Jean Dupont", accessMode = Schema.AccessMode.READ_ONLY)
    private String appUserFullName;
}

