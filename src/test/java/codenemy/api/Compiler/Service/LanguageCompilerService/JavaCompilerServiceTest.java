//package codenemy.api.Compiler.Service.LanguageCompilerService;
//
//import codenemy.api.Compiler.Model.MultipleTestCaseResults;
//import codenemy.api.Compiler.Model.Request;
//import codenemy.api.Compiler.Model.SingleTestCaseResult;
//import codenemy.api.Compiler.Model.TestCaseResult;
//import codenemy.api.Problem.model.Problem;
//import codenemy.api.Problem.model.ProblemLanguage;
//import codenemy.api.Util.CompilerUtility;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//
//import static org.mockito.Mockito.*;
//
///**
// * @author chaudhary samir zafar
// * @version 1.0
// * @since 18/01/2023
// */
//@ExtendWith(MockitoExtension.class)
//public class JavaCompilerServiceTest {
//    JavaCompilerService sut;
//    @Mock
//    CompilerUtility compilerUtility;
//    @Mock
//    Problem problem;
//    @Mock
//    ProblemLanguage problemLanguage;
//
//    static final String JAVA_VERSION = "15.0.2";
//
//    @BeforeEach
//    void setUp(){
//        sut = new JavaCompilerService(compilerUtility);
//    }
//
//    @Test
//    void executeSingleTestCase() {
//        // Given
//        String script = "testRunOne";
//        String language = "java";
//
//        Request request = new Request("java", "import java.util.*;\n code", 1, "", 0);
//        TestCaseResult testCaseResult = new TestCaseResult(0, null);
//
//        when(problemLanguage.getTestRunOne()).thenReturn(script);
//        //when(request.language()).thenReturn(language);
//        when(compilerUtility.getTestCaseResult("TestRun.java",  "import java.util.*;\r\n" + script + " code\r\n", language, JAVA_VERSION))
//                .thenReturn(testCaseResult);
//
//        // When
//        sut.executeSingleTestCase(request, problem, problemLanguage);
//
//        // Then
//        verify(compilerUtility).getTestCaseResult("TestRun.java", "import java.util.*;\r\n" + script + " code\r\n", language, JAVA_VERSION);
//        verify(compilerUtility).calculateSingleTestResultWithResponse(problem, testCaseResult);
//    }
//
//    @Test
//    void executeSingleTestCaseWithError() {
//        // Given
//        String script = "testRunOne";
//        String language = "java";
//
//        Request request = new Request("java", "code", 1, "", 0);
//        TestCaseResult testCaseResult = new TestCaseResult(0, null);
//        testCaseResult.setError(Arrays.asList("Error", "Error"));
//
//        when(problemLanguage.getTestRunOne()).thenReturn(script);
//        when(compilerUtility.getTestCaseResult("TestRun.java", script + request.script() + "\r\n", language, JAVA_VERSION))
//                .thenReturn(testCaseResult);
//
//        // When
//        SingleTestCaseResult singleTestCaseResult = sut.executeSingleTestCase(request, problem, problemLanguage);
//
//        // Then
//        verify(compilerUtility).getTestCaseResult("TestRun.java", script + request.script() + "\r\n", language, JAVA_VERSION);
//        verifyNoMoreInteractions(compilerUtility);
//        Assertions.assertEquals(testCaseResult.getError(), singleTestCaseResult.error());
//    }
//
//    @Test
//    void executeAllTestCases() {
//        // Given
//        String script = "testRunAll";
//        String language = "java";
//
//        Request request = new Request("java", "code", 1, "", 0);
//        TestCaseResult testCaseResult = new TestCaseResult(0, null);
//
//        when(problemLanguage.getTestRunAll()).thenReturn(script);
//        when(compilerUtility.getTestCaseResult("TestRun.java", script + request.script() + "\r\n", language, JAVA_VERSION))
//                .thenReturn(testCaseResult);
//
//        // When
//        sut.executeAllTestCases(request, problem, problemLanguage);
//
//        // Then
//        verify(compilerUtility).getTestCaseResult("TestRun.java", script + request.script() + "\r\n", language, JAVA_VERSION);
//        verify(compilerUtility).calculateAllTestResultsWithResponse(problem, testCaseResult);
//    }
//
//    @Test
//    void executeAllTestCasesWithError() {
//        // Given
//        String script = "testRunAll";
//        String language = "java";
//
//        Request request = new Request("java", "code", 1, "", 0);
//        TestCaseResult testCaseResult = new TestCaseResult(0, null);
//        testCaseResult.setError(Arrays.asList("Error", "Error"));
//
//        when(problemLanguage.getTestRunAll()).thenReturn(script);
//        when(compilerUtility.getTestCaseResult("TestRun.java", script + request.script() + "\r\n", language, JAVA_VERSION))
//                .thenReturn(testCaseResult);
//
//        // When
//        MultipleTestCaseResults multipleTestCaseResults = sut.executeAllTestCases(request, problem, problemLanguage);
//
//        // Then
//        verify(compilerUtility).getTestCaseResult("TestRun.java", script + request.script() + "\r\n", language, JAVA_VERSION);
//        verifyNoMoreInteractions(compilerUtility);
//        Assertions.assertEquals(testCaseResult.getError(), multipleTestCaseResults.getError());
//    }
//}
