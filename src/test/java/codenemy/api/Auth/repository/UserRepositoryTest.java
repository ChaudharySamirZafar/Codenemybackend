package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
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

        // given
        String username = "samirzafar";

        User expectedUser = new User(0, "samirzafar", "samir786",0,0,null, null);
        sut.save(expectedUser);

        // when
        User userFound = sut.findByUsername(username);

        // then
        assertThat(userFound.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(userFound.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(userFound.getProblemPoints()).isEqualTo(expectedUser.getProblemPoints());
        assertThat(userFound.getCompetitionPoints()).isEqualTo(expectedUser.getCompetitionPoints());
        assertThat(userFound.getImage()).isEqualTo(expectedUser.getImage());
    }

    @Test
    void itShouldReturnNullIfUserDoesNotExist() {

        // given
        String username = "samirzafar";

        // when
        User userFound = sut.findByUsername(username);

        // then
        assertThat(userFound).isNull();
    }
}