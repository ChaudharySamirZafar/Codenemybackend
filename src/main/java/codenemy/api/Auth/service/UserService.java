package codenemy.api.Auth.service;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
import codenemy.api.Auth.model.UserDTO;
import codenemy.api.Auth.model.UserDTOMapper;
import codenemy.api.Auth.repository.RoleRepo;
import codenemy.api.Auth.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserDTOMapper userDTOMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepo.findByUsername(username);

        validateUser(user);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public void saveUser(User user) {

        log.info("Saving user : {} to the database", user.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        addRoleToUser(user.getUsername(), "ROLE_USER");
    }

    public UserDTO updateUser(int userId, String newUserName, String newPassword) {

        Optional<User> user = userRepo.findById(userId);

        if (user.isEmpty()) return null;

        if (!newPassword.isEmpty()) {
            user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
        }
        if (!newUserName.isEmpty()) {
            user.get().setUsername(newUserName);
        }

        userRepo.save(user.get());

        return userDTOMapper.apply(user.get());
    }

    @Override
    public void saveRole(Role role) {

        log.info("Saving role : {} to the database", role.getName());
        roleRepo.save(role);
    }

    @Override
    public UserDTO addRoleToUser(String username, String roleName) {

        User user = userRepo.findByUsername(username);
        validateUser(user);
        Role role = roleRepo.findByName(roleName);
        validateRole(role);

        log.info("Adding role : {} to user : {}", user.getUsername(), role.getName());

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<Role>());
            user.getRoles().add(role);
        }
        else {
            boolean roleFound = user.getRoles()
                    .stream()
                    .anyMatch(userRole -> userRole.getName().equals(roleName));

            if (!roleFound) user.getRoles().add(role);
        }

        return userDTOMapper.apply(user);
    }

    @Override
    public UserDTO getUser(String username) {

        log.info("fetching user : {}", username);
        return userDTOMapper.apply(userRepo.findByUsername(username));
    }

    @Override
    public List<UserDTO> getUsers() {

        log.info("fetching all users");
        return userRepo.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public UserDTO transformUserObject(User user) {

        return userDTOMapper.apply(user);
    }

    private void validateUser(User user){

        if (user == null){
            log.info("User was not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else
        {
            log.info("User was found");
        }
    }

    private void validateRole(Role role){

        if (role == null){
            log.info("Role was not found in the database");
            throw new NoSuchElementException("Role not found in the database");
        }
        else
        {
            log.info("User was found");
        }
    }
}
