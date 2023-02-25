package codenemy.api.Auth.repository;

import codenemy.api.Auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    @Modifying(clearAutomatically = true)
    @Query("update _User SET problemPoints = :problemPoints where id = :userId")
    int updateProblemPoints(int problemPoints, int userId);
}
