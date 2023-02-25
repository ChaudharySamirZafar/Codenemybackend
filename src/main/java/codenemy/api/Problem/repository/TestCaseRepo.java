package codenemy.api.Problem.repository;

import codenemy.api.Problem.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public interface TestCaseRepo extends JpaRepository<TestCase, Integer> {
}
