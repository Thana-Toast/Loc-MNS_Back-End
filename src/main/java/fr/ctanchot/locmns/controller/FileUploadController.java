package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.security.IsAdmin;
import fr.ctanchot.locmns.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import fr.ctanchot.locmns.service.OrphanFileCleanupService;

@RestController
@CrossOrigin
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "11. Fichiers", description = "Endpoints d'upload de fichiers (images, documents) - Réservé aux Administrateurs")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final OrphanFileCleanupService orphanFileCleanupService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @IsAdmin
    @Operation(summary = "Téléverser un fichier", description = "Permet de téléverser un fichier (image de matériel, notice PDF). Retourne le nom du fichier généré et son URL relative d'accès. Réservé aux administrateurs.")
    @ApiResponse(responseCode = "200", description = "Fichier téléversé avec succès")
    @ApiResponse(responseCode = "400", description = "Fichier invalide ou extension non autorisée")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Droits administrateur requis")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le fichier est vide. Veuillez sélectionner un fichier valide."));
        }

        try {
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = "/uploads/" + fileName;

            return ResponseEntity.ok(Map.of(
                "fileName", fileName,
                "fileUrl", fileUrl,
                "message", "Fichier téléversé avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cleanup-orphans")
    @IsAdmin
    @Operation(summary = "Nettoyer les fichiers orphelins", description = "Déclenche le nettoyage manuel de tous les fichiers du dossier /uploads non utilisés en BDD et datant de plus de 6h. Réservé aux administrateurs.")
    @ApiResponse(responseCode = "200", description = "Nettoyage déclenché avec succès")
    @ApiResponse(responseCode = "403", description = "Accès refusé - Droits administrateur requis")
    public ResponseEntity<?> cleanupOrphans() {
        orphanFileCleanupService.cleanOrphanFiles();
        return ResponseEntity.ok(Map.of("message", "Nettoyage des fichiers orphelins déclenché avec succès."));
    }
}
