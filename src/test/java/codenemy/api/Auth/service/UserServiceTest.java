package codenemy.api.Auth.service;

import codenemy.api.Auth.model.User;
import codenemy.api.Auth.model.Role;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService sut;
    @Mock
    private UserRepo mockUserRepo;
    @Mock
    private RoleRepo mockRoleRepo;
    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        sut = new UserService(mockUserRepo, mockRoleRepo, mockBCryptPasswordEncoder);
    }

    @Test
    void loadUserByUsername() {

        assertThatThrownBy(() -> sut.loadUserByUsername("samir"))
                .hasMessage("User not found in the database")
                .isInstanceOf(UsernameNotFoundException.class);

        verify(mockUserRepo).findByUsername("samir");
    }

    @Test
    void loadUserByUsernameUserExists() {
        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        User testUser = new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samir")).thenReturn(testUser);

        UserDetails result = sut.loadUserByUsername("samir");

        verify(mockUserRepo).findByUsername("samir");
        assertThat(result.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(result.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(result.getAuthorities().size()).isEqualTo(listOfRoles.size());
    }

    @Test
    void saveUser() {

        // given
        User testUser = new User(1, "samirzafar", "samir786", 0, 0, null, null);

        // when
        when(mockUserRepo.findByUsername(testUser.getUsername())).thenReturn(testUser);
        when(mockRoleRepo.findByName("ROLE_USER")).thenReturn(new Role(1, "ROLE_USER"));

        sut.saveUser(testUser);

        // then
        ArgumentCaptor<User> coderArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(mockUserRepo).save(coderArgumentCaptor.capture());

        User capturedUser = coderArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(testUser);
    }

    @Test
    void saveRole() {

        // given
        Role testRole = new Role(1, "TEST_ROLE");
        // when
        sut.saveRole(testRole);
        // then
        ArgumentCaptor<Role> roleArgumentCaptor =
                ArgumentCaptor.forClass(Role.class);

        verify(mockRoleRepo).save(roleArgumentCaptor.capture());

        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(capturedRole).isEqualTo(testRole);
    }

    @Test
    void addRoleToUser() {
        User testUser = new User(1, "samirzafar", "samir786", 0, 0, null, null);
        Role testRole = new Role(1, "ROLE_USER");

        when(mockUserRepo.findByUsername(testUser.getUsername())).thenReturn(testUser);
        when(mockRoleRepo.findByName(testRole.getName())).thenReturn(testRole);

        // when
        sut.addRoleToUser("samirzafar", "ROLE_USER");

        // then
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

        // when
        sut.addRoleToUser("samir", "TEST_ROLE");
        // then
        verify(mockUserRepo).findByUsername("samir");
        verify(mockRoleRepo).findByName("TEST_ROLE");
        assertThat(testUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    void addRoleToUserWhenUserExistsWithRoles() {

        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        // Given
        User testUser =
                new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samirzafar")).thenReturn(testUser);

        Role testRole = new Role(1, "TEST_ROLE_V2");
        when(mockRoleRepo.findByName("TEST_ROLE_V2")).thenReturn(testRole);

        // when
        sut.addRoleToUser("samirzafar", "TEST_ROLE_V2");

        // then
        verify(mockUserRepo).findByUsername("samirzafar");
        verify(mockRoleRepo).findByName("TEST_ROLE_V2");
        assertThat(testUser.getRoles().size()).isEqualTo(3);
    }

    @Test
    void addRoleToUserWhenTheyAlreadyHaveThatRole() {

        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        // Given
        User testUser =
                new User(1, "samirzafar", "samir786", 0, 0, null, listOfRoles);
        when(mockUserRepo.findByUsername("samirzafar")).thenReturn(testUser);

        Role testRole = new Role(1, "TEST_ROLE");
        when(mockRoleRepo.findByName("TEST_ROLE")).thenReturn(testRole);

        // when
        sut.addRoleToUser("samirzafar", "TEST_ROLE");

        // then
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

        List<Role> listOfRoles = new ArrayList<Role>();
        listOfRoles.add(new Role(1, "TEST_ROLE"));
        listOfRoles.add(new Role(2, "TEST"));

        // Given
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

        // when
        sut.getUser("samir");
        // then
        verify(mockUserRepo).findByUsername("samir");
    }

    @Test
    void getUsers() {

        // when
        sut.getUsers();
        // then
        verify(mockUserRepo).findAll();
    }
}

