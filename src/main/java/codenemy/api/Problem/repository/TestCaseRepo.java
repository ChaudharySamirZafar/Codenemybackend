package codenemy.api.Problem.repository;

import codenemy.api.Problem.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepo extends JpaRepository<TestCase, Integer> {
}
