package codenemy.api.Problem.controller;

import codenemy.api.Problem.model.*;
import codenemy.api.Problem.service.ProblemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class ProblemControllerTest {
    ProblemController sut;

    @Mock
    ProblemService problemService;

    @BeforeEach
    void setUp(){

        sut = new ProblemController(problemService);
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

        when(problemService.getAllProblems()).thenReturn(problemArrayList);

        // When
        ResponseEntity<List<Problem>> result = sut.getAllProblems();

        // Then
        assertEquals(problemArrayList.size(), Objects.requireNonNull(result.getBody()).size());
        for(int i =0; i < 5; i++) {
            Problem elementFromLocalArrayList = problemArrayList.get(i);
            Problem elementFromResult = result.getBody().get(i);

            assertEquals(elementFromLocalArrayList.getName(), elementFromResult.getName());
            assertEquals(elementFromLocalArrayList.getId(), elementFromResult.getId());
            assertEquals(elementFromLocalArrayList.getDescription(), elementFromResult.getDescription());

            // Ensure the tags are the same
            for(int j = 0; j < elementFromLocalArrayList.getTags().size(); j++) {
                Tag localTag = elementFromLocalArrayList.getTags().get(j);
                Tag resultTag = elementFromResult.getTags().get(j);

                assertEquals(localTag.getId(), resultTag.getId());
                assertEquals(localTag.getTag(), resultTag.getTag());
            }

            // Ensure the testcases are the same
            for(int j = 0; j < elementFromLocalArrayList.getTestCases().size(); j++) {
                TestCase localTestCase = elementFromLocalArrayList.getTestCases().get(j);
                TestCase resultTestCase = elementFromResult.getTestCases().get(j);

                assertEquals(localTestCase.getId(), resultTestCase.getId());
                assertEquals(localTestCase.getInput(), resultTestCase.getInput());
                assertEquals(localTestCase.getOutput(), resultTestCase.getOutput());
                assertEquals(localTestCase.getProblemId(), resultTestCase.getProblemId());
                assertEquals(localTestCase.getProblem(), resultTestCase.getProblem());

                String toString = "TestCase{" +
                        "id='" + localTestCase.getId() + '\'' +
                        ", input='" + localTestCase.getInput() + '\'' +
                        ", output='" + localTestCase.getOutput() + '\'';

                assertEquals(toString, resultTestCase.toString());
            }

            // Ensure the problem languages
            for(int j = 0; j < elementFromLocalArrayList.getProblemLanguages().size(); j++) {
                ProblemLanguage localProblemLang = elementFromLocalArrayList.getProblemLanguages().get(j);
                ProblemLanguage resultProblemLang = elementFromResult.getProblemLanguages().get(j);

                assertEquals(localProblemLang.getId(), resultProblemLang.getId());
                assertEquals(localProblemLang.getProblem_id(), resultProblemLang.getProblem_id());
                assertEquals(localProblemLang.getLanguage_id(), resultProblemLang.getLanguage_id());
                assertEquals(localProblemLang.getCode(), resultProblemLang.getCode());
                assertEquals(localProblemLang.getProblem(), resultProblemLang.getProblem());
                assertEquals(localProblemLang.getLanguage(), resultProblemLang.getLanguage());
                assertEquals(localProblemLang.getTestRunOne(), resultProblemLang.getTestRunOne());
                assertEquals(localProblemLang.getTestRunAll(), resultProblemLang.getTestRunAll());

                Language localLang = localProblemLang.getLanguage();
                Language resultLang = resultProblemLang.getLanguage();

                assertEquals(localLang.getId(), resultLang.getId());
                assertEquals(localLang.getProgrammingLanguage(), resultLang.getProgrammingLanguage());
            }


            assertEquals(elementFromLocalArrayList.getTestCases(), elementFromResult.getTestCases());
            assertEquals(elementFromLocalArrayList.getDifficulty(), elementFromResult.getDifficulty());
        }
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    void getAllProblemsThatUserHasSolved(){

        // Given
        ArrayList<Problem> problemArrayList = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            Problem problem =
                    new Problem(i, "problem"+i, "description"+i, List.of(new Tag(i, "tag" + i)), null,
                            null, "easy", i % 2 == 0);

            if (i  % 2 == 0) problem.setSolved(true);

            problemArrayList
                    .add(problem);

        }

        when(problemService.getAllProblemsLoggedIn(0)).thenReturn(problemArrayList.stream().filter(Problem::isSolved).toList());

        // When
        ResponseEntity<List<Problem>> result = sut.getAllProblemsWithSolvedInformation(0);

        // Then
        assertEquals(2, Objects.requireNonNull(result.getBody()).size());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getSpecificProblem(){

        // Given
        ArrayList<Problem> problemArrayList = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            problemArrayList
                    .add(new Problem(i, "problem"+i, "description"+i, List.of(new Tag(i, "tag" + i)), null,
                            null, "easy", false));
        }

        when(problemService.getProblem(0)).thenReturn(problemArrayList.get(0));
        when(problemService.getProblem(1)).thenReturn(problemArrayList.get(1));

        // When
        ResponseEntity<Problem> result = sut.getProblem(0);
        ResponseEntity<Problem> resultTwo = sut.getProblem(1);

        // Then
        assertEquals(problemArrayList.get(0), result.getBody());
        assertEquals(problemArrayList.get(1), resultTwo.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getCount(){

        // Given
        ArrayList<Problem> problemArrayList = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            problemArrayList
                    .add(new Problem(i, "problem"+i, "description"+i, List.of(new Tag(i, "tag" + i)), null,
                            null, "easy", false));
        }

        when(problemService.getAllProblems()).thenReturn(problemArrayList);

        // When
        ResponseEntity<Integer> result = sut.getProblemCount();

        // Then
        assertEquals(problemArrayList.size(), result.getBody());
    }
}
