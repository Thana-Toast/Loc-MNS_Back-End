package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.RoleDao;
import fr.ctanchot.locmns.dto.AppUserDTO;
import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserDao appUserDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.appUserDao = appUserDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> findAll() {
        return appUserDao.findAll();
    }

    public Optional<AppUser> findById(Long id) {
        return appUserDao.findById(id);
    }

    public AppUser save(AppUser user) {
        if (user.getId() == null) {
            user.setCreationDate(LocalDateTime.now());
            user.setIsAnonymized(false);
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getRoles() == null) {
                user.setRoles(new java.util.ArrayList<>());
            }
            roleDao.findByLabel("ROLE_USER").ifPresent(role -> user.getRoles().add(role));
        }
        return appUserDao.save(user);
    }

    public void deleteById(Long id) {
        appUserDao.deleteById(id);
    }

    public AppUser update(Long id, AppUserDTO userUpdate) {
        Optional<AppUser> existingUser = appUserDao.findById(id);
        if (existingUser.isPresent()) {
            AppUser userToUpdate = existingUser.get();
            
            userToUpdate.setLastName(userUpdate.getLastName());
            userToUpdate.setFirstName(userUpdate.getFirstName());
            userToUpdate.setEmail(userUpdate.getEmail());
            
            if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
                userToUpdate.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            }

            if (userUpdate.getConsentDate() != null) {
                userToUpdate.setConsentDate(userUpdate.getConsentDate());
            }

            if (userUpdate.getDepartureDate() != null) {
                userToUpdate.setDepartureDate(userUpdate.getDepartureDate());
            }

            if (userUpdate.getIsAnonymized() != null) {
                userToUpdate.setIsAnonymized(userUpdate.getIsAnonymized());
            }

            return appUserDao.save(userToUpdate);
        }
        return null;
    }
}
