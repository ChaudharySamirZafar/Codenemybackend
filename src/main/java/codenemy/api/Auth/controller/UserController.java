package codenemy.api.Auth.controller;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
import codenemy.api.Auth.service.UserService;
import codenemy.api.Util.Utility;
import com.auth0.jwt.JWT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    public ResponseEntity<User> addRoleToUser(@RequestBody RoleToUserForm form){
        return ResponseEntity.ok().body(userService.addRoleToUser(form.getUsername(), form.getRoleName()));
    }

    @PostMapping("/user/changeDetails")
    public ResponseEntity<HashMap<String, Object>> changeUserDetails(
            @RequestParam int userId, @RequestParam String newUserName, @RequestParam String newPassword, HttpServletRequest request, HttpServletResponse response){
        User user = userService.updateUser(userId, newUserName, newPassword);

        HashMap<String, Object> responseBody = createAccessTokenAndRefreshToken(user, request);
        responseBody.put("userModel", user);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/user/register")
    public ResponseEntity<HashMap<String, Object>> registerUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.saveUser(user);

        HashMap<String, Object> responseBody = createAccessTokenAndRefreshToken(user, request);
        responseBody.put("userModel", user);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private HashMap<String, Object> createAccessTokenAndRefreshToken(User user, HttpServletRequest request) {

        HashMap<String, Object> tokens = new HashMap<>();

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

        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        return tokens;
    }
}

@Getter
@Setter
class RoleToUserForm {
    private String username;
    private String roleName;
}
