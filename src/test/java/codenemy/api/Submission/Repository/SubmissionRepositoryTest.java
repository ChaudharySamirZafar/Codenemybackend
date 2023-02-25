package codenemy.api.Submission.Repository;

import codenemy.api.Auth.model.User;
import codenemy.api.Auth.repository.UserRepo;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.repository.ProblemRepository;
import codenemy.api.Submission.Model.Submission;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@DataJpaTest
public class SubmissionRepositoryTest {
    @Autowired
    private SubmissionRepository sut;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProblemRepository problemRepository;
    private Submission mockSubmission;

    @AfterEach
    void tearDown(){

        sut.deleteAll();
        userRepo.deleteAll();
        problemRepository.deleteAll();
    }


    @Test
    void findSubmissionByUser(){

        // Given
        User user = new User();
        user.setId(0);
        userRepo.save(user);

        Problem problem = new Problem();
        problem.setId(0);
        problemRepository.save(problem);

        mockSubmission =
                new Submission(0, user, problem, LocalDateTime.now(), "", 100,0 );
        sut.save(mockSubmission);

        // When
        Set<Integer> result = sut.findSubmissionByUser(user.getId());

        assertEquals(1, result.size());
        assertTrue(result.contains(1));
    }

    @Test
    void findSubmissionByUserAndProblem(){

        // Given
        User user = new User();
        user.setId(0);
        userRepo.save(user);

        Problem problem = new Problem();
        problem.setId(0);
        problemRepository.save(problem);

        mockSubmission =
                new Submission(0, user, problem, LocalDateTime.now(), "", 0,0 );
        sut.save(mockSubmission);

        // When
        List<Submission> result = sut.findSubmissionByUserAndProblem(user.getId(), problem.getId());

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(mockSubmission));
    }
}
