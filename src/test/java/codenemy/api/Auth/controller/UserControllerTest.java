package codenemy.api.Auth.controller;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
import codenemy.api.Auth.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    UserController sut;
    @Mock
    UserService userService;
    public final static Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    @BeforeEach
    void setUp(){
        sut = new UserController(userService);
    }

    @Test
    void getAllUsers(){
        // Given
        List<User> userArrayList = new ArrayList<User>();
        for(int i = 0; i < 5; i++) {
            userArrayList.add(new User(0, "username"+i, "password"+i, i, i, null, null));
        }
        when(userService.getUsers()).thenReturn(userArrayList);

        // When
        ResponseEntity<List<User>> result = sut.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userArrayList, result.getBody());
    }

    @Test
    void addRoleToUser(){
        // Given
        User user = new User(0, "testuser", "testpassword", 1000, 1500, null,
                new ArrayList<>());
        Role role = new Role(1, "admin");
        user.getRoles().add(role);

        RoleToUserForm roleToUserForm = new RoleToUserForm();
        roleToUserForm.setRoleName("admin");
        roleToUserForm.setUsername("testuser");

        when(userService.addRoleToUser(roleToUserForm.getUsername(), roleToUserForm.getRoleName())).thenReturn(user);

        // When
        ResponseEntity<User> result = sut.addRoleToUser(roleToUserForm);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    void changeUserDetails(){
        // Given
        User user = new User(0, "testuser", "testpassword", 1000, 1500, null,
                new ArrayList<>());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(userService.updateUser(0, "", "")).thenReturn(user);

        // When
        ResponseEntity<HashMap<String, Object>> result = sut.changeUserDetails(0, "", "", request, response);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService).updateUser(0, "", "");

        String access_token = (String) Objects.requireNonNull(result.getBody()).get("access_token");

        DecodedJWT decodedJWT;
        JWTVerifier verifier = JWT.require(algorithm).build();
        decodedJWT = verifier.verify(access_token);

        assertEquals(user.getUsername(), decodedJWT.getSubject());
        assertNull(decodedJWT.getClaim("roles").asString());
        assertEquals("http://localhost", decodedJWT.getIssuer());
    }

    @Test
    void registerUser() throws IOException {
        // Given
        User user = new User(0, "testuser", "testpassword", 1000, 1500, new byte[0],
                new ArrayList<>());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // When
        ResponseEntity<HashMap<String, Object>> result = sut.registerUser(user, request, response);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService).saveUser(user);

        String access_token = (String) Objects.requireNonNull(result.getBody()).get("access_token");

        DecodedJWT decodedJWT;
        JWTVerifier verifier = JWT.require(algorithm).build();
        decodedJWT = verifier.verify(access_token);

        assertEquals(user.getUsername(), decodedJWT.getSubject());
        assertNull(decodedJWT.getClaim("roles").asString());
        assertEquals("http://localhost", decodedJWT.getIssuer());
    }
}
