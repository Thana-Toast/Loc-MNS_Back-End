package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.AppUserDTO;
import fr.ctanchot.locmns.dto.AppUserMapper;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import fr.ctanchot.locmns.security.AppUserDetails;
import fr.ctanchot.locmns.security.JwtService;
import fr.ctanchot.locmns.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "01. Authentification", description = "Endpoints de connexion et d'inscription des utilisateurs")
public class AuthController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    @PostMapping("/sign-in")
    @Operation(summary = "Inscription d'un nouvel utilisateur", description = "Crée un nouvel utilisateur et chiffre son mot de passe en base de données.")
    @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès")
    @ApiResponse(responseCode = "400", description = "Format de requête invalide ou email déjà existant")
    public ResponseEntity<AppUserDTO> signIn(@Validated @RequestBody AppUserDTO userDtoToInsert) {
        AppUser userToInsert = appUserMapper.toEntity(userDtoToInsert);
        AppUser savedUser = appUserService.save(userToInsert);
        return new ResponseEntity<>(appUserMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion de l'utilisateur", description = "Vérifie les identifiants et génère un jeton JWT contenant les rôles de l'utilisateur.")
    @ApiResponse(responseCode = "200", description = "Authentification réussie, retourne le jeton JWT")
    @ApiResponse(responseCode = "401", description = "Identifiants (email ou mot de passe) incorrects")
    public ResponseEntity<String> login(@RequestBody AppUser loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationProvider.authenticate(authToken);
            AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();

            String roles = appUserDetails.getUser().getRoles().stream()
                    .map(Role::getLabel)
                    .collect(Collectors.joining(","));

            String jwt = jwtService.generateToken(
                    appUserDetails.getUser().getEmail(),
                    roles,
                    appUserDetails.getUser().getFirstName(),
                    appUserDetails.getUser().getLastName()
            );

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Email ou mot de passe incorrect", HttpStatus.UNAUTHORIZED);
        }
    }
}


