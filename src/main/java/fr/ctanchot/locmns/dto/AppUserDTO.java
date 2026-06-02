package fr.ctanchot.locmns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente les informations de compte d'un utilisateur")
public class AppUserDTO {

    @Schema(description = "Identifiant unique de l'utilisateur", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50)
    @Schema(description = "Nom de famille de l'utilisateur", example = "Dupont")
    private String lastName;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 50)
    @Schema(description = "Prénom de l'utilisateur", example = "Jean")
    private String firstName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 255)
    @Schema(description = "Adresse e-mail unique de l'utilisateur (sert d'identifiant de connexion)", example = "jean.dupont@locmns.fr")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Mot de passe de l'utilisateur (en écriture uniquement)", example = "superSecret123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Schema(description = "Date de création du compte", example = "2026-06-01T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(description = "Date de consentement RGPD pour le traitement des données", example = "2026-06-01T10:05:00")
    private LocalDateTime consentDate;

    @Schema(description = "Date de départ de l'utilisateur de l'organisation", example = "2026-12-31T18:00:00")
    private LocalDateTime departureDate;

    @Schema(description = "Indique si le compte a été anonymisé (RGPD)", example = "false")
    private Boolean isAnonymized;

    @Schema(description = "Liste des rôles / permissions attribués à l'utilisateur", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private List<String> roles;
}

