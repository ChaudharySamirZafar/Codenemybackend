package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
