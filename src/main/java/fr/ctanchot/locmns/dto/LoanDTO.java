package fr.ctanchot.locmns.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private Long id;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate startDate;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate endDate;

    private LocalDate actualReturnDate;

    @NotNull(message = "La demande de prêt est obligatoire")
    private Long loanRequestId;

    private String equipmentSerialNumber; // Pour l'affichage Front
    private String appUserFullName;        // Pour l'affichage Front
}
