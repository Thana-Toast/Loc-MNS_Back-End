package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.TicketStatus;
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
@Schema(description = "Représente un ticket d'incident ou anomalie signalé sur un équipement")
public class TicketDTO {

    @Schema(description = "Identifiant unique du ticket d'incident", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Date et heure de création du ticket", example = "2026-06-01T14:20:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime date;

    @NotBlank(message = "Le titre (label) est obligatoire")
    @Schema(description = "Titre court résumant le problème rencontré", example = "Écran noir au démarrage")
    private String label;

    @NotBlank(message = "La description est obligatoire")
    @Schema(description = "Description détaillée des symptômes de la panne ou du dysfonctionnement", example = "Lors du démarrage, les ventilateurs tournent mais l'écran reste noir. Aucun voyant d'état n'indique de problème de batterie.")
    private String description;

    @NotNull(message = "L'équipement est obligatoire")
    @Schema(description = "Identifiant du matériel physique affecté par l'anomalie", example = "10")
    private Long equipmentId;

    @Schema(description = "Numéro de série de l'équipement défectueux", example = "SN-DELL-5420-12345", accessMode = Schema.AccessMode.READ_ONLY)
    private String equipmentSerialNumber;

    @NotNull(message = "L'utilisateur est obligatoire")
    @Schema(description = "Identifiant de l'utilisateur ayant signalé l'incident", example = "2")
    private Long appUserId;

    @Schema(description = "Nom complet du déclarant de l'incident", example = "Jean Dupont", accessMode = Schema.AccessMode.READ_ONLY)
    private String appUserFullName;

    @Schema(description = "Statut actuel du cycle de vie de l'incident", example = "OPEN", accessMode = Schema.AccessMode.READ_ONLY)
    private TicketStatus currentStatus;
}

