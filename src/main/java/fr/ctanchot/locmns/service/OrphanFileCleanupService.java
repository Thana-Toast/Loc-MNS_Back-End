package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.DocumentDao;
import fr.ctanchot.locmns.dao.ModelDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrphanFileCleanupService {

    private final ModelDao modelDao;
    private final DocumentDao documentDao;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // S'exécute automatiquement toutes les nuits à 3h00 du matin
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanOrphanFiles() {
        Path uploadDirPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadDirPath)) {
            return;
        }

        // Récupère l'ensemble des noms de fichiers enregistrés en base de données
        Set<String> usedFileNames = new HashSet<>();

        List<String> modelImages = modelDao.findAllImagePaths();
        for (String path : modelImages) {
            if (path != null && !path.isBlank()) {
                usedFileNames.add(extractFileName(path));
            }
        }

        List<String> docFiles = documentDao.findAllFilePaths();
        for (String path : docFiles) {
            if (path != null && !path.isBlank()) {
                usedFileNames.add(extractFileName(path));
            }
        }

        // Seuil d'ancienneté : fichiers créés il y a plus de 6 heures pour ne pas supprimer un fichier en cours d'upload
        Instant threshold = Instant.now().minus(6, ChronoUnit.HOURS);

        int deletedCount = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadDirPath)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    String fileName = file.getFileName().toString();
                    FileTime lastModified = Files.getLastModifiedTime(file);

                    // Si le fichier n'est référencé dans aucune table et date de plus de 6h -> suppression
                    if (!usedFileNames.contains(fileName) && lastModified.toInstant().isBefore(threshold)) {
                        if (Files.deleteIfExists(file)) {
                            deletedCount++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du nettoyage automatique des fichiers orphelins : " + e.getMessage());
        }

        if (deletedCount > 0) {
            System.out.println("[Nettoyage Orphelins] " + deletedCount + " fichier(s) inutile(s) supprimé(s) avec succès.");
        }
    }

    private String extractFileName(String path) {
        int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    }
}
