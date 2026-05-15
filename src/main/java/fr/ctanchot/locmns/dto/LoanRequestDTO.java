package fr.ctanchot.locmns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {

    private Long id;

    private LocalDateTime date;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "L'utilisateur est obligatoire")
    private Long appUserId;

    private String appUserFullName; // Pour l'affichage Front

    @NotNull(message = "L'équipement est obligatoire")
    private Long equipmentId;

    private String equipmentSerialNumber; // Pour l'affichage Front
}
