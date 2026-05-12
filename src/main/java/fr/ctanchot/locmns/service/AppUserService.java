package fr.ctanchot.locmns.service;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;

    @Autowired
    public AppUserService(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
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
        }
        return appUserDao.save(user);
    }

    public void deleteById(Long id) {
        appUserDao.deleteById(id);
    }

    public AppUser update(Long id, AppUser userUpdate) {
        Optional<AppUser> existingUser = appUserDao.findById(id);
        if (existingUser.isPresent()) {
            userUpdate.setId(id);
            userUpdate.setCreationDate(existingUser.get().getCreationDate());
            return appUserDao.save(userUpdate);
        }
        return null;
    }
}
