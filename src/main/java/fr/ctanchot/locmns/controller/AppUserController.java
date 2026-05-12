package fr.ctanchot.locmns.controller;

import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class AppUserController {

    protected AppUserService appUserService;

    @Autowired
    public AppUserController (AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/user/list")
    public List<AppUser> getAll() {
        return appUserService.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AppUser> get(@PathVariable Long id) {

        Optional<AppUser> optionalAppUser = appUserService.findById(id);

        if (optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalAppUser.get(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<AppUser> create(
        @RequestBody
        AppUser userToInsert
    ) {
        AppUser savedUser = appUserService.save(userToInsert);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Optional<AppUser> optionalAppUser = appUserService.findById(id);

        if (optionalAppUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appUserService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<AppUser> update(
        @PathVariable Long id,
        @RequestBody
        AppUser userToUpdate
    ) {

        AppUser updatedUser = appUserService.update(id, userToUpdate);

        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
