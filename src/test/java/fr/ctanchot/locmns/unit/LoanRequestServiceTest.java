package fr.ctanchot.locmns.unit;

import fr.ctanchot.locmns.dao.AppUserDao;
import fr.ctanchot.locmns.dao.EquipmentDao;
import fr.ctanchot.locmns.dao.LoanRequestDao;
import fr.ctanchot.locmns.model.*;
import fr.ctanchot.locmns.service.LoanRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanRequestServiceTest {

    @Mock
    private LoanRequestDao loanRequestDao;

    @Mock
    private AppUserDao appUserDao;

    @Mock
    private EquipmentDao equipmentDao;

    @InjectMocks
    private LoanRequestService loanRequestService;

    private AppUser user;
    private Equipment equipment;
    private Category category;
    private Model model;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setLabel("Ordinateurs Portables");

        model = new Model();
        model.setId(1L);
        model.setCategory(category);

        equipment = new Equipment();
        equipment.setId(10L);
        equipment.setModel(model);

        user = new AppUser();
        user.setId(2L);
        user.setLastName("Dupont");
        user.setFirstName("Jean");

        loanRequest = new LoanRequest();
        loanRequest.setAppUser(user);
        loanRequest.setEquipment(equipment);
        loanRequest.setDescription("Besoin pro");
    }

    @Test
    void save_ShouldSucceed_WhenUserHasAccessToCategory() {
        Role role = new Role();
        role.setId(1L);
        role.setLabel("ROLE_USER");
        role.setAuthorizedCategories(List.of(category));

        user.setRoles(List.of(role));

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findById(equipment.getId())).thenReturn(Optional.of(equipment));
        when(loanRequestDao.save(any(LoanRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanRequest result = loanRequestService.save(loanRequest);

        assertNotNull(result);
        assertEquals(user, result.getAppUser());
        assertEquals(equipment, result.getEquipment());
        verify(loanRequestDao, times(1)).save(loanRequest);
    }

    @Test
    void save_ShouldThrowException_WhenUserDoesNotHaveAccessToCategory() {
        Category otherCategory = new Category();
        otherCategory.setId(99L);
        otherCategory.setLabel("Casques VR");

        Role role = new Role();
        role.setId(1L);
        role.setLabel("ROLE_USER");
        role.setAuthorizedCategories(List.of(otherCategory));

        user.setRoles(List.of(role));

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findById(equipment.getId())).thenReturn(Optional.of(equipment));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanRequestService.save(loanRequest);
        });

        assertTrue(exception.getMessage().contains("L'utilisateur n'a pas les droits pour louer des équipements de la catégorie"));
        verify(loanRequestDao, never()).save(any(LoanRequest.class));
    }

    @Test
    void save_ShouldThrowException_WhenUserHasNoRoles() {
        user.setRoles(new ArrayList<>());

        when(appUserDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(equipmentDao.findById(equipment.getId())).thenReturn(Optional.of(equipment));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanRequestService.save(loanRequest);
        });

        assertEquals("L'utilisateur n'a aucun rôle associé.", exception.getMessage());
        verify(loanRequestDao, never()).save(any(LoanRequest.class));
    }
}
