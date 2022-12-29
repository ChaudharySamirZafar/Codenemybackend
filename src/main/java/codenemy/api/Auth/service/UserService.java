package codenemy.api.Auth.service;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 28/12/2022
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username);

        validateUser(user);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving user : {} to the database", user.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        addRoleToUser(user.getUsername(), "ROLE_USER");
        return user;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role : {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public User addRoleToUser(String username, String roleName) {

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

        return user;
    }

    @Override
    public User getUser(String username) {
        log.info("fetching user : {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("fetching all users");
        return userRepo.findAll();
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
