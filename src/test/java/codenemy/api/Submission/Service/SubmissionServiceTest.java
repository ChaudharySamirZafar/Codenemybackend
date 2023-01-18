package codenemy.api.Submission.Service;

import codenemy.api.Auth.model.User;
import codenemy.api.Auth.repository.UserRepo;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Submission.Model.Submission;
import codenemy.api.Submission.Repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {
    private SubmissionService sut;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private UserRepo userRepo;
    @BeforeEach
    void setUp() {

        sut = new SubmissionService(submissionRepository, userRepo);
    }

    @Test
    void addSubmission(){
        // Given
        User user =
                new User(1, "samirzafar", "password", 0, 0, null, null);
        Problem problem =
                new Problem(2, "testProblem", "testDescription", null, null, null, "easy", false);
        Submission submission
                = new Submission(0, user, problem, LocalDateTime.now(), "", 50, 500);
        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(submissionRepository.findSubmissionByUserAndProblem(user.getId(), problem.getId())).thenReturn(new ArrayList<Submission>());

        // When
        sut.addSubmission(submission, user.getId(), multipleTestCaseResults);

        // Then
        assertEquals(500, user.getProblemPoints());
        assertEquals(user.getProblemPoints(), multipleTestCaseResults.getNewProblemPoints());
    }

    @Test
    void addSubmissionWIthSecondSubmissionWithHigherPoints(){
        // Given
        User user =
                new User(1, "samirzafar", "password", 400, 0, null, null);
        Problem problem =
                new Problem(2, "testProblem", "testDescription", null, null, null, "easy", false);
        Submission submission
                = new Submission(0, user, problem, LocalDateTime.now(), "", 50, 500);
        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();

        Submission submissionTwo =
                new Submission(0, user, problem, LocalDateTime.now(), "", 40, 400);

        Submission submissionThree =
                new Submission(0, user, problem, LocalDateTime.now(), "", 40, 400);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(submissionRepository.findSubmissionByUserAndProblem(user.getId(), problem.getId())).thenReturn(Arrays.asList(submissionTwo, submissionThree));

        // When
        sut.addSubmission(submission, user.getId(), multipleTestCaseResults);

        // Then
        assertEquals(500, user.getProblemPoints());
        assertEquals(user.getProblemPoints(), multipleTestCaseResults.getNewProblemPoints());
    }

    @Test
    void addSubmissionWIthSecondSubmissionWithLowerPoints(){
        // Given
        User user =
                new User(1, "samirzafar", "password", 600, 0, null, null);
        Problem problem =
                new Problem(2, "testProblem", "testDescription", null, null, null, "easy", false);
        Submission submission
                = new Submission(0, user, problem, LocalDateTime.now(), "", 50, 500);
        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();

        Submission submissionTwo =
                new Submission(0, user, problem, LocalDateTime.now(), "", 40, 400);

        Submission submissionThree =
                new Submission(0, user, problem, LocalDateTime.now(), "", 40, 600);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(submissionRepository.findSubmissionByUserAndProblem(user.getId(), problem.getId())).thenReturn(Arrays.asList(submissionTwo, submissionThree));

        // When
        sut.addSubmission(submission, user.getId(), multipleTestCaseResults);

        // Then
        assertEquals(600, user.getProblemPoints());
        assertEquals(user.getProblemPoints(), multipleTestCaseResults.getNewProblemPoints());
    }
}
