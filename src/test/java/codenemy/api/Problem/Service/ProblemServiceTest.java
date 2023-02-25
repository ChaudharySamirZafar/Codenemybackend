package codenemy.api.Problem.Service;

import codenemy.api.Problem.model.*;
import codenemy.api.Problem.repository.ProblemRepository;
import codenemy.api.Problem.service.ProblemService;
import codenemy.api.Submission.Repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class ProblemServiceTest {
    ProblemService sut;
    @Mock
    ProblemRepository problemRepository;
    @Mock
    SubmissionRepository submissionRepository;

    @BeforeEach
    void setUp(){

        sut = new ProblemService(problemRepository, submissionRepository);
    }

    @Test
    void getAllProblems(){

        // Given
        ArrayList<Problem> problemArrayList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            problemArrayList
                    .add(new Problem(
                            i,
                            "problem"+i,
                            "description"+i,
                            List.of(new Tag(i, "tag" + i), new Tag(i, "tag2" + i)),
                            List.of(new TestCase(i, "input" + i, "output" + i, i, null)),
                            List.of(new ProblemLanguage(0, 0, 0, "code"+i, null, new Language(i, "lang"+i), "", "")),
                            "easy",
                            false));
        }

        when(problemRepository.findAll()).thenReturn(problemArrayList);

        // When
        List<Problem> result = sut.getAllProblems();

        // Then
        assertEquals(problemArrayList.size(), result.size());
        assertEquals(problemArrayList, result);
        verify(problemRepository).findAll();
    }

    @Test
    void getAllProblemsLoggedIn(){

        // Given
        ArrayList<Problem> problemArrayList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            problemArrayList
                    .add(new Problem(
                            i,
                            "problem"+i,
                            "description"+i,
                            List.of(new Tag(i, "tag" + i), new Tag(i, "tag2" + i)),
                            List.of(new TestCase(i, "input" + i, "output" + i, i, null)),
                            List.of(new ProblemLanguage(0, 0, 0, "code"+i, null, new Language(i, "lang"+i), "", "")),
                            "easy",
                            false));
        }

        Set<Integer> integerSet = new HashSet<>();
        integerSet.add(4);
        integerSet.add(3);

        when(problemRepository.findAll()).thenReturn(problemArrayList);
        when(submissionRepository.findSubmissionByUser(0)).thenReturn(integerSet);

        // When
        List<Problem> result = sut.getAllProblemsLoggedIn(0);

        // Then
        assertEquals(problemArrayList.size(), result.size());

        for(int i = 0; i < 5; i++){
            Problem localElement = problemArrayList.get(i);
            Problem resultElement = result.get(i);

            assertEquals(localElement.getId(), resultElement.getId());
            assertEquals(localElement.getName(), resultElement.getName());
            assertEquals(localElement.getTags(), resultElement.getTags());
            assertEquals(localElement.getTestCases(), resultElement.getTestCases());
            assertEquals(localElement.getProblemLanguages(), resultElement.getProblemLanguages());
            assertEquals(localElement.getDifficulty(), resultElement.getDifficulty());

            if (i < 3) {
                assertFalse(resultElement.isSolved());
            }
            else {
                assertTrue(resultElement.isSolved());
            }
        }

        verify(problemRepository).findAll();
        verify(submissionRepository).findSubmissionByUser(0);
    }

    @Test
    void getAllProblemsLoggedInEmptyProblemList(){

        // Given
        when(problemRepository.findAll()).thenReturn(new ArrayList<Problem>());
        when(submissionRepository.findSubmissionByUser(0)).thenReturn(null);

        // When
        sut.getAllProblemsLoggedIn(0);

        // Then
        verify(problemRepository).findAll();
        verify(submissionRepository).findSubmissionByUser(0);
    }

    @Test
    void getProblem(){

        // Given
        Problem problem = new Problem(
                1,
                "problem"+1,
                "description"+1,
                List.of(new Tag(1, "tag" + 1), new Tag(1, "tag2" + 1)),
                List.of(new TestCase(1, "input" + 1, "output" + 1, 1, null)),
                List.of(new ProblemLanguage(0, 0, 0, "code"+1, null, new Language(1, "lang"+1), "", "")),
                "easy",
                false);

        when(problemRepository.findById(1)).thenReturn(Optional.of(problem));

        // When
        Problem result = sut.getProblem(1);

        // Then
        assertEquals(problem, result);
        verify(problemRepository).findById(1);
    }

}
