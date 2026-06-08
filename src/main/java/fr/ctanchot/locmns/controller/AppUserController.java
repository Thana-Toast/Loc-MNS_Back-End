package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.AppUserDTO;
import fr.ctanchot.locmns.dto.AppUserMapper;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Tag(name = "02. Utilisateurs", description = "Gestion des comptes utilisateurs (CRUD)")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;

    public AppUserController(AppUserService appUserService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.appUserMapper = appUserMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Récupérer la liste des utilisateurs", description = "Retourne la liste complète de tous les utilisateurs enregistrés au format DTO.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Droits insuffisants")
    public List<AppUserDTO> getAll() {
        return appUserService.findAll().stream()
                .map(appUserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par son ID", description = "Retourne un utilisateur unique en fonction de son identifiant numérique.")
    @ApiResponse(responseCode = "200", description = "Utilisateur trouvé et renvoyé")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    public ResponseEntity<AppUserDTO> get(@PathVariable Long id) {

        Optional<AppUser> optionalAppUser = appUserService.findById(id);

        if (optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appUserMapper.toDto(optionalAppUser.get()), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel utilisateur", description = "Enregistre un nouvel utilisateur en base de données. Le mot de passe sera automatiquement haché.")
    @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données utilisateur invalides")
    public ResponseEntity<AppUserDTO> create(
        @Validated @RequestBody
        AppUserDTO userDtoToInsert
    ) {
        AppUser userToInsert = appUserMapper.toEntity(userDtoToInsert);
        AppUser savedUser = appUserService.save(userToInsert);
        return new ResponseEntity<>(appUserMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime définitivement un utilisateur de la base de données par son ID.")
    @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Optional<AppUser> optionalAppUser = appUserService.findById(id);

        if (optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appUserService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour les informations d'un utilisateur existant. Si un mot de passe est renseigné, il sera haché.")
    @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    public ResponseEntity<AppUserDTO> update(
        @PathVariable Long id,
        @Validated @RequestBody
        AppUserDTO userDtoToUpdate
    ) {
        AppUser updatedUser = appUserService.update(id, userDtoToUpdate);

        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appUserMapper.toDto(updatedUser), HttpStatus.OK);
    }
}

