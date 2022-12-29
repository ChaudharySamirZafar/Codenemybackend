package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

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

        User expectedUser = new User(1, "samirzafar", "samir786",0,0,null, null);
        sut.save(expectedUser);

        // when
        User userFound = sut.findByUsername(username);

        // then
        assertThat(userFound).isEqualTo(expectedUser);
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