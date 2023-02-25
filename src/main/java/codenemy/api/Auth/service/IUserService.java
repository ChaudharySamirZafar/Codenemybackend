package codenemy.api.Auth.service;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
import codenemy.api.Auth.model.UserDTO;

import java.util.List;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public interface IUserService {
    void saveUser(User user);
    void saveRole(Role role);
    UserDTO addRoleToUser(String username, String roleName);
    UserDTO getUser(String username);
    List<UserDTO> getUsers();
}
