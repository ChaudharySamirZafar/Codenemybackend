package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

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

        // given
        String roleName = "ROLE_ADMIN";
        Role expectedRole = new Role(1, roleName);
        sut.save(expectedRole);

        // when
        Role roleFound = sut.findByName(roleName);

        // then
        assertThat(roleFound).isEqualTo(expectedRole);
    }

    @Test
    void itShouldReturnNullIfRoleDoesNotExist() {

        // given
        String roleName = "ROLE_ADMIN";

        // when
        Role roleFound = sut.findByName(roleName);

        // then
        assertThat(roleFound).isNull();
    }
}