package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
class UserRepositoryTest {
    @Autowired
    private UserRepo sut;

    @AfterEach
    void tearDown() {

        sut.deleteAll();
    }

    @Test
    void itShouldReturnTheUserIfTheUserExists() {

        // Given
        String username = "samirzafar";

        User expectedUser =
                new User(0, "samirzafar", "samir786",0,0,null, null);
        sut.save(expectedUser);

        // When
        User userFound = sut.findByUsername(username);

        // Then
        Assertions.assertEquals(expectedUser, userFound);
    }

    @Test
    void itShouldReturnNullIfUserDoesNotExist() {

        // Given
        String username = "samirzafar";

        // When
        User userFound = sut.findByUsername(username);

        // Then
        assertThat(userFound).isNull();
    }
}