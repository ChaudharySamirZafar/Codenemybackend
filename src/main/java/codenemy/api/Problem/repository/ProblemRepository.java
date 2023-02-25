package codenemy.api.Problem.repository;

import codenemy.api.Problem.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

}
