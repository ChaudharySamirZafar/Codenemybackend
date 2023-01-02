package codenemy.api.Problem.repository;

import codenemy.api.Problem.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 30/12/2022
 */
public interface TestCaseRepo extends JpaRepository<TestCase, Integer> {
}
