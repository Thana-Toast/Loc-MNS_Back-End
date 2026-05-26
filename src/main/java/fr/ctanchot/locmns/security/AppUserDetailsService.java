package fr.ctanchot.locmns.security;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserDao appUserDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> optionalAppUser = appUserDao.findByEmail(email);

        if (optionalAppUser.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur introuvable avec l'email : " + email);
        }

        return new AppUserDetails(optionalAppUser.get());
    }
}
