package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.dto.AppUserDTO;
import fr.ctanchot.locmns.dto.AppUserMapper;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.service.AppUserService;
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
public class AppUserController {

    protected AppUserService appUserService;
    protected AppUserMapper appUserMapper;

    @Autowired
    public AppUserController (AppUserService appUserService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.appUserMapper = appUserMapper;
    }

    @GetMapping("/list")
    public List<AppUserDTO> getAll() {
        return appUserService.findAll().stream()
                .map(appUserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> get(@PathVariable Long id) {

        Optional<AppUser> optionalAppUser = appUserService.findById(id);

        if (optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appUserMapper.toDto(optionalAppUser.get()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> create(
        @Validated @RequestBody
        AppUserDTO userDtoToInsert
    ) {
        AppUser userToInsert = appUserMapper.toEntity(userDtoToInsert);
        AppUser savedUser = appUserService.save(userToInsert);
        return new ResponseEntity<>(appUserMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Optional<AppUser> optionalAppUser = appUserService.findById(id);

        if (optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appUserService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
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
