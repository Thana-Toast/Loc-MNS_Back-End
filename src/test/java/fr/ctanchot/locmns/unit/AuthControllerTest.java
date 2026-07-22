package fr.ctanchot.locmns.unit;

import fr.ctanchot.locmns.controller.AuthController;
import fr.ctanchot.locmns.dto.AppUserDTO;
import fr.ctanchot.locmns.dto.AppUserMapper;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import fr.ctanchot.locmns.security.AppUserDetails;
import fr.ctanchot.locmns.security.JwtService;
import fr.ctanchot.locmns.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppUserService appUserService;

    @Mock
    private AppUserMapper appUserMapper;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void shouldReturnJwtTokenOnSuccessfulLogin() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId(1L);
        mockUser.setEmail("admin@loc-alhost.com");
        mockUser.setFirstName("Marty");
        mockUser.setLastName("FRIEDMAN");
        Role role = new Role(1L, "ROLE_ADMIN", List.of(), List.of());
        mockUser.setRoles(List.of(role));

        AppUserDetails userDetails = new AppUserDetails(mockUser);
        UsernamePasswordAuthenticationToken authResult =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationProvider.authenticate(any())).thenReturn(authResult);
        when(jwtService.generateToken(eq("admin@loc-alhost.com"), eq("ROLE_ADMIN"), eq("Marty"), eq("FRIEDMAN")))
                .thenReturn("fake-jwt-token");

        String jsonRequest = "{\"email\":\"admin@loc-alhost.com\",\"password\":\"password\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));
    }

    @Test
    void shouldReturn401OnBadCredentials() throws Exception {
        when(authenticationProvider.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        String jsonRequest = "{\"email\":\"wrong@locmns.fr\",\"password\":\"wrongpass\"}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email ou mot de passe incorrect"));
    }
}
