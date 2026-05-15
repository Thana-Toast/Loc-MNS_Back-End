package fr.ctanchot.locmns.dto;

import fr.ctanchot.locmns.model.AppUser;
import fr.ctanchot.locmns.model.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AppUserMapper {

    public AppUserDTO toDto(AppUser entity) {
        if (entity == null) return null;
        
        AppUserDTO dto = new AppUserDTO();
        dto.setId(entity.getId());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setEmail(entity.getEmail());
        dto.setCreationDate(entity.getCreationDate());
        dto.setConsentDate(entity.getConsentDate());
        dto.setDepartureDate(entity.getDepartureDate());
        dto.setIsAnonymized(entity.getIsAnonymized());
        
        if (entity.getRoles() != null) {
            dto.setRoles(entity.getRoles().stream()
                    .map(Role::getLabel)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public AppUser toEntity(AppUserDTO dto) {
        if (dto == null) return null;
        
        AppUser entity = new AppUser();
        entity.setId(dto.getId());
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setConsentDate(dto.getConsentDate());
        entity.setDepartureDate(dto.getDepartureDate());
        entity.setIsAnonymized(dto.getIsAnonymized());
        
        return entity;
    }
}
