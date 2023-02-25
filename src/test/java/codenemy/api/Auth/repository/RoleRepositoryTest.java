package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepo sut;

    @AfterEach
    void tearDown() {

        sut.deleteAll();
    }

    @Test
    void itShouldReturnTheRoleIfTheRoleExists() {

        // Given
        String roleName = "ROLE_ADMIN";
        Role expectedRole = new Role(1, roleName);
        sut.save(expectedRole);

        // When
        Role roleFound = sut.findByName(roleName);

        // Then
        assertThat(roleFound).isEqualTo(expectedRole);
    }

    @Test
    void itShouldReturnNullIfRoleDoesNotExist() {

        // Given
        String roleName = "ROLE_ADMIN";

        // When
        Role roleFound = sut.findByName(roleName);

        // Then
        assertThat(roleFound).isNull();
    }
}