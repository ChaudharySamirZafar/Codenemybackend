package codenemy.api.Auth.model;

import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getProblemPoints(),
                user.getCompetitionPoints(),
                user.getImage(),
                user.getRoles());
    }
}
