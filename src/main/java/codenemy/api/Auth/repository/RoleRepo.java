package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 28/12/2022
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
