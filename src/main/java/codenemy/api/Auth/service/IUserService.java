package codenemy.api.Auth.service;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;

import java.util.List;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 28/12/2022
 */
public interface IUserService {
    User saveUser(User user);
    Role saveRole(Role role);
    User addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
