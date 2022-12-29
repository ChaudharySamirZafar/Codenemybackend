package codenemy.api.Auth.controller;

import codenemy.api.Auth.model.User;
import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.service.UserService;
import codenemy.api.Util.Utility;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 28/12/2022
 */
@RestController
@RequestMapping( "/api")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>>getAllUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/users/addRoleToUser")
    public ResponseEntity<User>addRoleToUser(@RequestBody RoleToUserForm form){
        return ResponseEntity.ok().body(userService.addRoleToUser(form.getUsername(), form.getRoleName()));
    }

    @PostMapping("/user/register")
    public ResponseEntity<HashMap<String, Object>> registerUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.saveUser(user);

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(Utility.algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(Utility.algorithm);

        HashMap<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        tokens.put("userModel", user);

        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @GetMapping("/user/refreshToken")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        DecodedJWT decodedJWT = Utility.decodeJWT(request, response);

        if (decodedJWT != null) {
            String username = decodedJWT.getSubject();

            User user = userService.getUser(username);

            String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .sign(Utility.algorithm);

            Utility.writeTokenValues(access_token, request.getHeader(AUTHORIZATION).substring("Bearer ".length()), response);
        }
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
