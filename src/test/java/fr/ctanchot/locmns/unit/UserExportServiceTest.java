package fr.ctanchot.locmns.unit;

import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import fr.ctanchot.locmns.service.UserExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserExportServiceTest {

    private UserExportService userExportService;

    @BeforeEach
    void setUp() {
        userExportService = new UserExportService();
    }

    @Test
    void shouldExportToCsvWithCorrectHeadersAndFormat() {
        Role role = new Role(1L, "ROLE_USER", java.util.Collections.emptyList(), java.util.Collections.emptyList());
        AppUser user = new AppUser();
        user.setId(1L);
        user.setLastName("DUPONT");
        user.setFirstName("Jean");
        user.setEmail("jean.dupont@locmns.fr");
        user.setRoles(List.of(role));
        user.setCreationDate(LocalDateTime.of(2026, 6, 1, 10, 0));
        user.setIsAnonymized(false);

        byte[] result = userExportService.exportToCsv(List.of(user));
        String csvContent = new String(result, StandardCharsets.UTF_8);

        assertTrue(csvContent.startsWith("\uFEFFID;Nom;Prénom;Email;Rôles"));
        assertTrue(csvContent.contains("1;DUPONT;Jean;jean.dupont@locmns.fr;ROLE_USER;2026-06-01T10:00:00;;;Non"));
    }

    @Test
    void shouldExportToXmlWithValidStructure() {
        Role role = new Role(2L, "ROLE_ADMIN", java.util.Collections.emptyList(), java.util.Collections.emptyList());
        AppUser user = new AppUser();
        user.setId(2L);
        user.setLastName("ADMIN");
        user.setFirstName("Super");
        user.setEmail("admin@locmns.fr");
        user.setRoles(List.of(role));
        user.setCreationDate(LocalDateTime.of(2026, 6, 1, 10, 0));
        user.setIsAnonymized(false);

        byte[] result = userExportService.exportToXml(List.of(user));
        String xmlContent = new String(result, StandardCharsets.UTF_8);

        assertTrue(xmlContent.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(xmlContent.contains("<users>"));
        assertTrue(xmlContent.contains("<lastName>ADMIN</lastName>"));
        assertTrue(xmlContent.contains("<firstName>Super</firstName>"));
        assertTrue(xmlContent.contains("<role>ROLE_ADMIN</role>"));
        assertTrue(xmlContent.contains("</users>"));
    }
}
