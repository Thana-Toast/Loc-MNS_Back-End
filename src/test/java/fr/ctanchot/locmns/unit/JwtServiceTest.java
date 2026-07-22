package fr.ctanchot.locmns.unit;

import fr.ctanchot.locmns.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private static final String TEST_SECRET = "yOvS42XQ@*dPBvSS$$m5Xtn*hl^C1#spVl61mLeo@";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET);
    }

    @Test
    void shouldGenerateAndExtractEmailFromToken() {
        String email = "admin@loc-alhost.com";
        String token = jwtService.generateToken(email, "ROLE_ADMIN", "Marty", "FRIEDMAN");

        assertNotNull(token);
        assertFalse(token.isBlank());

        String extractedEmail = jwtService.extractEmail(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void shouldFailExtractOnInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(Exception.class, () -> {
            jwtService.extractEmail(invalidToken);
        });
    }
}
