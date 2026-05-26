package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.AppUserDTO;
import fr.ctanchot.locmns.dto.AppUserMapper;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import fr.ctanchot.locmns.security.AppUserDetails;
import fr.ctanchot.locmns.security.JwtService;
import fr.ctanchot.locmns.service.AppUserService;
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
public class AuthController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    @PostMapping("/sign-in")
    public ResponseEntity<AppUserDTO> signIn(@Validated @RequestBody AppUserDTO userDtoToInsert) {
        AppUser userToInsert = appUserMapper.toEntity(userDtoToInsert);
        AppUser savedUser = appUserService.save(userToInsert);
        return new ResponseEntity<>(appUserMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AppUser loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationProvider.authenticate(authToken);
            AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();

            String roles = appUserDetails.getUser().getRoles().stream()
                    .map(Role::getLabel)
                    .collect(Collectors.joining(","));

            String jwt = jwtService.generateToken(appUserDetails.getUser().getEmail(), roles);

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Email ou mot de passe incorrect", HttpStatus.UNAUTHORIZED);
        }
    }
}

