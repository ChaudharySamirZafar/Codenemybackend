package codenemy.api.Problem.repository;

import codenemy.api.Problem.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 30/12/2022
 */
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

}
