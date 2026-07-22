package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Génère un fichier CSV (avec BOM UTF-8 pour la prise en charge d'Excel)
     */
    public byte[] exportToCsv(List<AppUser> users) {
        StringBuilder csv = new StringBuilder();
        // BOM UTF-8 pour qu'Excel ouvre le fichier sans problème d'encodage des accents
        csv.append("\uFEFF");
        // En-tête CSV
        csv.append("ID;Nom;Prénom;Email;Rôles;Date de création;Consentement RGPD;Date de départ;Anonymisé\n");

        for (AppUser user : users) {
            String rolesStr = user.getRoles() != null
                    ? user.getRoles().stream().map(Role::getLabel).collect(Collectors.joining(","))
                    : "";

            csv.append(user.getId() != null ? user.getId() : "").append(";")
               .append(escapeCsv(user.getLastName())).append(";")
               .append(escapeCsv(user.getFirstName())).append(";")
               .append(escapeCsv(user.getEmail())).append(";")
               .append(escapeCsv(rolesStr)).append(";")
               .append(user.getCreationDate() != null ? user.getCreationDate().format(DATE_FORMATTER) : "").append(";")
               .append(user.getConsentDate() != null ? user.getConsentDate().format(DATE_FORMATTER) : "").append(";")
               .append(user.getDepartureDate() != null ? user.getDepartureDate().format(DATE_FORMATTER) : "").append(";")
               .append(Boolean.TRUE.equals(user.getIsAnonymized()) ? "Oui" : "Non")
               .append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Génère un document XML structuré représentant les utilisateurs
     */
    public byte[] exportToXml(List<AppUser> users) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<users>\n");

        for (AppUser user : users) {
            xml.append("  <user>\n");
            xml.append("    <id>").append(user.getId() != null ? user.getId() : "").append("</id>\n");
            xml.append("    <lastName>").append(escapeXml(user.getLastName())).append("</lastName>\n");
            xml.append("    <firstName>").append(escapeXml(user.getFirstName())).append("</firstName>\n");
            xml.append("    <email>").append(escapeXml(user.getEmail())).append("</email>\n");
            
            xml.append("    <roles>\n");
            if (user.getRoles() != null) {
                for (Role role : user.getRoles()) {
                    xml.append("      <role>").append(escapeXml(role.getLabel())).append("</role>\n");
                }
            }
            xml.append("    </roles>\n");

            xml.append("    <creationDate>").append(user.getCreationDate() != null ? user.getCreationDate().format(DATE_FORMATTER) : "").append("</creationDate>\n");
            xml.append("    <consentDate>").append(user.getConsentDate() != null ? user.getConsentDate().format(DATE_FORMATTER) : "").append("</consentDate>\n");
            xml.append("    <departureDate>").append(user.getDepartureDate() != null ? user.getDepartureDate().format(DATE_FORMATTER) : "").append("</departureDate>\n");
            xml.append("    <isAnonymized>").append(Boolean.TRUE.equals(user.getIsAnonymized())).append("</isAnonymized>\n");
            xml.append("  </user>\n");
        }

        xml.append("</users>");

        return xml.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
