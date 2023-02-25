package codenemy.api.Auth.service;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
import codenemy.api.Auth.model.UserDTO;
import codenemy.api.Auth.model.UserDTOMapper;
import codenemy.api.Auth.repository.RoleRepo;
import codenemy.api.Auth.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService sut;
    @Mock
    private UserRepo mockUserRepo;
    @Mock
    private RoleRepo mockRoleRepo;
    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    private final UserDTOMapper userDTOMapper = new UserDTOMapper();

    @BeforeEach
    void setUp() {

        sut = new UserService(mockUserRepo, mockRoleRepo, mockBCryptPasswordEncoder, userDTOMapper);
    }

    @Test
    void loadUserByUsername() {

        // When
        assertThatThrownBy(() -> sut.loadUserByUsername("samir"))
                .hasMessage("User not found in the database")
                .isInstanceOf(UsernameNotFoundException.class);

        // Then
        verify(mockUserRepo).findByUsername("samir");
    }

    @Test
    void loadUserByUsernameUserExists() {

        // Given
        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        User testUser =
                new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samir")).thenReturn(testUser);

        // When
        UserDetails result = sut.loadUserByUsername("samir");

        // Then
        verify(mockUserRepo).findByUsername("samir");
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getPassword(), testUser.getPassword());
        assertEquals(result.getAuthorities().size(), listOfRoles.size());
    }

    @Test
    void saveUser() {

        // Given
        User testUser = new User(1, "samirzafar", "samir786", 0, 0, null, null);

        // When
        when(mockUserRepo.findByUsername(testUser.getUsername())).thenReturn(testUser);
        when(mockRoleRepo.findByName("ROLE_USER")).thenReturn(new Role(1, "ROLE_USER"));

        sut.saveUser(testUser);

        // Then
        ArgumentCaptor<User> coderArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(mockUserRepo).save(coderArgumentCaptor.capture());

        User capturedUser = coderArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(testUser);
    }

    @Test
    void saveRole() {

        // Given
        Role testRole = new Role(1, "TEST_ROLE");

        // When
        sut.saveRole(testRole);

        // Then
        ArgumentCaptor<Role> roleArgumentCaptor =
                ArgumentCaptor.forClass(Role.class);

        verify(mockRoleRepo).save(roleArgumentCaptor.capture());

        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(capturedRole).isEqualTo(testRole);
    }

    @Test
    void addRoleToUser() {

        // Given
        User testUser = new User(1, "samirzafar", "samir786", 0, 0, null, null);
        Role testRole = new Role(1, "ROLE_USER");

        when(mockUserRepo.findByUsername(testUser.getUsername())).thenReturn(testUser);
        when(mockRoleRepo.findByName(testRole.getName())).thenReturn(testRole);

        // When
        sut.addRoleToUser("samirzafar", "ROLE_USER");

        // Then
        verify(mockUserRepo).findByUsername(testUser.getUsername());
        verify(mockRoleRepo).findByName(testRole.getName());
    }

    @Test
    void addRoleToUserWhenUserExistsWithNoRoles() {

        // Given
        User testUser = new User(1, "samirzafar", "samir786", 0, 0, null, null);
        when(mockUserRepo.findByUsername("samir")).thenReturn(testUser);

        Role testRole = new Role(1, "TEST_ROLE");
        when(mockRoleRepo.findByName("TEST_ROLE")).thenReturn(testRole);

        // When
        sut.addRoleToUser("samir", "TEST_ROLE");

        // Then
        verify(mockUserRepo).findByUsername("samir");
        verify(mockRoleRepo).findByName("TEST_ROLE");
        assertThat(testUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    void addRoleToUserWhenUserExistsWithRoles() {

        // Given
        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        User testUser =
                new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samirzafar")).thenReturn(testUser);

        Role testRole = new Role(1, "TEST_ROLE_V2");
        when(mockRoleRepo.findByName("TEST_ROLE_V2")).thenReturn(testRole);

        // When
        sut.addRoleToUser("samirzafar", "TEST_ROLE_V2");

        // Then
        verify(mockUserRepo).findByUsername("samirzafar");
        verify(mockRoleRepo).findByName("TEST_ROLE_V2");
        assertThat(testUser.getRoles().size()).isEqualTo(3);
    }

    @Test
    void addRoleToUserWhenTheyAlreadyHaveThatRole() {

        // Given
        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        User testUser =
                new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samirzafar")).thenReturn(testUser);

        Role testRole = new Role(1, "TEST_ROLE");
        when(mockRoleRepo.findByName("TEST_ROLE")).thenReturn(testRole);

        // When
        sut.addRoleToUser("samirzafar", "TEST_ROLE");

        // Then
        verify(mockUserRepo).findByUsername("samirzafar");
        verify(mockRoleRepo).findByName("TEST_ROLE");
        assertThat(testUser.getRoles().size()).isEqualTo(listOfRoles.size());
    }

    @Test
    void addRoleToUserThatDoesntExist(){

        assertThatThrownBy(() -> sut.addRoleToUser("samir", "test"))
            .hasMessage("User not found in the database")
            .isInstanceOf(UsernameNotFoundException.class);

        verify(mockUserRepo).findByUsername("samir");
    }

    @Test
    void addRoleThatDoesntExistToUser(){

        // Given
        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        User testUser =
                new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samirzafar")).thenReturn(testUser);

        // When
        assertThatThrownBy(() -> sut.addRoleToUser("samirzafar", "test"))
                .hasMessage("Role not found in the database")
                .isInstanceOf(NoSuchElementException.class);

        // Then
        verify(mockUserRepo).findByUsername("samirzafar");
    }

    @Test
    void getUser() {

        // Given
        User testUser =
                new User(1, "samir", "samir786", 0, 0, null, null);
        when(mockUserRepo.findByUsername("samir")).thenReturn(testUser);

        // When
        sut.getUser("samir");

        // Then
        verify(mockUserRepo).findByUsername("samir");
    }

    @Test
    void getUsers() {

        // When
        sut.getUsers();

        // Then
        verify(mockUserRepo).findAll();
    }

    @Test
    void updateUser() {

        // Given
        int userId = 5;
        String newUserName = "updatedUserName";
        String newPassword = "newPassword";
        User user = new User(5, "", "", 0, 0, null, null);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(user));
        when(mockBCryptPasswordEncoder.encode(newPassword)).thenReturn(newPassword);

        // When
        UserDTO result = sut.updateUser(userId, newUserName, newPassword);

        // Then
        assertEquals(newUserName, result.username());

        verify(mockUserRepo).findById(userId);
        verify(mockUserRepo).save(user);
    }

    @Test
    void updateUserThatDoesNotExist() {

        // Given
        int userId = 5;
        String newUserName = "updatedUserName";
        String newPassword = "newPassword";

        // When
        sut.updateUser(userId, newUserName, newPassword);

        // Then
        verify(mockUserRepo).findById(userId);
    }
}

