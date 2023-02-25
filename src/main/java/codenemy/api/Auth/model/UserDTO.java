package codenemy.api.Auth.model;

import java.util.Collection;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public record UserDTO(
        int id, String username, int problemPoints, int competitionPoints,
        byte[] image, Collection<Role> roles)
{

}
