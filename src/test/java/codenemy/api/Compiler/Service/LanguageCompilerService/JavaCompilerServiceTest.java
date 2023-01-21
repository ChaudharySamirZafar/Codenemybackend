package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import codenemy.api.Util.CompilerUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
@ExtendWith(MockitoExtension.class)
public class JavaCompilerServiceTest {
    JavaCompilerService sut;
    @Mock
    CompilerUtility compilerUtility;

    @BeforeEach
    void setUp(){
        sut = new JavaCompilerService(compilerUtility);
    }

    @Test
    void executeSingleTestCase() {
        // Given
        String script = "script";
        Request request =
                new Request("java", script, 0, "testuser", 0);
        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "", false);
        TestCaseResult testCaseResult = mock(TestCaseResult.class);

        when(compilerUtility.retrieveTestCaseResult(request, null)).thenReturn(testCaseResult);

        // When
        sut.executeSingleTestCase(request, script, problem);

        // Then
        verify(compilerUtility).createNewFile("TestRun.java");
        verify(compilerUtility).writeScriptToFile(script);
        verify(compilerUtility).startProcess("javac TestRun.java");
        verify(compilerUtility).startProcess("java TestRun");
        verify(compilerUtility).deleteFile("TestRun.class");
        verify(compilerUtility).deleteFile("TestRun.java");
        verify(compilerUtility).deleteFile("Solution.class");
        verify(compilerUtility).deleteFile("results_"+request.username()+".txt");
        verify(compilerUtility, atLeast(2)).retrieveTestCaseResult(request, null);
        verify(compilerUtility).calculateSingleTestResultWithResponse(problem, testCaseResult);
    }

    @Test
    void executeAllTestCases() {
        // Given
        String script = "script";
        Request request =
                new Request("java", script, 0, "testuser", 0);
        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "", false);
        TestCaseResult testCaseResult = mock(TestCaseResult.class);

        when(compilerUtility.retrieveTestCaseResult(request, null)).thenReturn(testCaseResult);

        // When
        sut.executeAllTestCases(request, script, problem);

        // Then
        verify(compilerUtility).createNewFile("TestRun.java");
        verify(compilerUtility).writeScriptToFile(script);
        verify(compilerUtility).startProcess("javac TestRun.java");
        verify(compilerUtility).startProcess("java TestRun");
        verify(compilerUtility).deleteFile("TestRun.class");
        verify(compilerUtility).deleteFile("TestRun.java");
        verify(compilerUtility).deleteFile("Solution.class");
        verify(compilerUtility).deleteFile("results_"+request.username()+".txt");
        verify(compilerUtility, atLeast(2)).retrieveTestCaseResult(request, null);
        verify(compilerUtility).calculateAllTestResultsWithResponse(problem, testCaseResult);
    }
}
