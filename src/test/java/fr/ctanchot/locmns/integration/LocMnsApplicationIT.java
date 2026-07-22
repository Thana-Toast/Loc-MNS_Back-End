package fr.ctanchot.locmns.integration;

import fr.ctanchot.locmns.controller.AppUserController;
import fr.ctanchot.locmns.controller.AuthController;
import fr.ctanchot.locmns.security.JwtService;
import fr.ctanchot.locmns.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LocMnsApplicationIT {

    @Autowired
    private AuthController authController;

    @Autowired
    private AppUserController appUserController;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private JwtService jwtService;

    @Test
    void contextLoadsAndBeansAreInitialized() {
        assertNotNull(authController, "Le contrôleur AuthController doit être chargé dans le contexte Spring.");
        assertNotNull(appUserController, "Le contrôleur AppUserController doit être chargé dans le contexte Spring.");
        assertNotNull(appUserService, "Le service AppUserService doit être chargé dans le contexte Spring.");
        assertNotNull(jwtService, "Le service JwtService doit être chargé dans le contexte Spring.");
    }
}
