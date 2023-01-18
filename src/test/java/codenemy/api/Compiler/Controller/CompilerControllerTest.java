package codenemy.api.Compiler.Controller;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Service.CompilerService;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.service.ProblemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
@ExtendWith(MockitoExtension.class)
public class CompilerControllerTest {
    CompilerController sut;
    @Mock
    ProblemService problemService;
    @Mock
    CompilerService compilerService;

    @BeforeEach
    void setUp(){
        sut = new CompilerController(problemService, compilerService);
    }

    @Test
    void runScriptForOneTestCase(){
        // Given
        Problem problem = new Problem();
        Request request =
                new Request("java", "helloWorld", 2, "samirzafar", 1);
        SingleTestCaseResult singleTestCaseResult =
                new SingleTestCaseResult("input", "o", "uo", List.of("oops"), false);
        when(problemService.getProblem(2)).thenReturn(problem);
        when(compilerService.runScriptForOneTestCase(request, problem)).thenReturn(singleTestCaseResult);

        // When
        ResponseEntity<SingleTestCaseResult>  result = sut.runScriptForOneTestCase(request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(singleTestCaseResult, result.getBody());
    }

    @Test
    void runScriptForAllTestCases(){
        // Given
        Problem problem = new Problem();
        Request request =
                new Request("java", "helloWorld", 2, "samirzafar", 1);
        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();
        multipleTestCaseResults.setPoints(100);
        multipleTestCaseResults.setPercentage(150);
        multipleTestCaseResults.setNewProblemPoints(15000);

        when(problemService.getProblem(2)).thenReturn(problem);
        when(compilerService.runScriptForAllTestCases(request, problem)).thenReturn(multipleTestCaseResults);

        // When
        ResponseEntity<MultipleTestCaseResults>  result = sut.runScriptForAllTestCases(request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(multipleTestCaseResults, result.getBody());
    }

    @Test
    void runScriptForAllTestCasesWithChallenge(){
        // Given
        Problem problem = new Problem();
        Request request =
                new Request("java", "helloWorld", 2, "samirzafar", 1);
        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();
        multipleTestCaseResults.setPoints(100);
        multipleTestCaseResults.setPercentage(150);
        multipleTestCaseResults.setNewProblemPoints(15000);

        when(problemService.getProblem(2)).thenReturn(problem);
        when(compilerService.runScriptForAllTestCasesWithChallenge(request, problem)).thenReturn(multipleTestCaseResults);

        // When
        ResponseEntity<MultipleTestCaseResults>  result = sut.runScriptForAllTestCasesWithChallenge(request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(multipleTestCaseResults, result.getBody());
    }
}
