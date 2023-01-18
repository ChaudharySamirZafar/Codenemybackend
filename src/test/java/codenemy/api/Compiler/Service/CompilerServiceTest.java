package codenemy.api.Compiler.Service;

import codenemy.api.Auth.model.User;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Service.LanguageCompilerService.JavaCompilerService;
import codenemy.api.Problem.model.*;
import codenemy.api.Submission.Service.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
@ComponentScan("codenemy.api.Problem.repository")
@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class CompilerServiceTest {
    private CompilerService sut;
    @Mock
    private CompilerServiceFactory mockCompilerServiceFactory;
    @Mock
    private SubmissionService mockSubmissionService;
    private Problem mockProblem;
    @Mock
    private JavaCompilerService mockJavaCompilerService;

    @BeforeEach
    void setUp(){
        sut = new CompilerService(mockCompilerServiceFactory, mockSubmissionService);

        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "Array"));
        tags.add(new Tag(2, "String"));

        ArrayList<TestCase> testCases = new ArrayList<>();
        testCases.add(new TestCase(1, "[2,7,11,15],9", "[0, 1]", 1, mockProblem));

        Language language = new Language(1, "java");

        ArrayList<ProblemLanguage> problemLanguages = new ArrayList<>();
        problemLanguages.add(new ProblemLanguage(1, 1, 1,
                "import java.util.Arrays;\n" +
                "import java.io.*;\n" +
                "\n" +
                "class Solution {\n" +
                "    public static int[] twoSum(int[] nums, int target) {\n" +
                "       return null;\n" +
                "    }\n" +
                "}", mockProblem, language,
                "public class TestRun {\n" +
                        "    public static void main(String[] args) throws IOException {\n" +
                        "        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(\"results_ENVIRONMENT_VAR.txt\")));\n" +
                        "        int[] nums = {2,7,11,15};\n" +
                        "        int target = 9;\n" +
                        "        writer.write(Arrays.toString(Solution.twoSum(nums, target)));\n" +
                        "        writer.newLine();\n" +
                        "        writer.close();\n" +
                        "    }\n" +
                        "}",
                "public class TestRun {\n" +
                        " public static void main(String[] args) throws IOException {\n" +
                        "        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(\"results_ENVIRONMENT_VAR.txt\")));\n" +
                        "        int[] nums = {2,7,11,15};\n" +
                        "        int target = 9;\n" +
                        "        writer.write(Arrays.toString(Solution.twoSum(nums, target)));\n" +
                        "        writer.newLine();\n" +
                        "        \n" +
                        "        nums = new int[]{3,2,4};\n" +
                        "        target = 6;\n" +
                        "        writer.write(Arrays.toString(Solution.twoSum(nums, target)));\n" +
                        "        writer.newLine();\n" +
                        "        \n" +
                        "        nums = new int[]{3,3,6};\n" +
                        "        target = 6;\n" +
                        "        writer.write(Arrays.toString(Solution.twoSum(nums, target)));\n" +
                        "        writer.newLine();\n" +
                        "        writer.close();\n" +
                        "    }\n" +
                        "}"));

        mockProblem = new Problem(1, "Two Sum", "Description", tags, testCases, problemLanguages, "Easy", false);

        User mockUser = new User(1, "samirzafar", "password", 0, 0, null, null);
    }

    @Test
    void runScriptForOneTestCase(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        String script = request.script() +
                "\n\n" +
                mockProblem
                        .getProblemLanguages()
                        .get(0)
                        .getTestRunOne()
                        .replace("results_ENVIRONMENT_VAR.txt", "results_" + request.username() + ".txt");


        SingleTestCaseResult singleTestCaseResult = new SingleTestCaseResult("[5]", "10", null, null, false);

        // When
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeSingleTestCase(request, script, mockProblem)).thenReturn(singleTestCaseResult);

        SingleTestCaseResult result = sut.runScriptForOneTestCase(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeSingleTestCase(request, script, mockProblem);
        assertThat(singleTestCaseResult).isEqualTo(result);
    }

    @Test
    void runScriptForAllTestCase(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        String script = request.script() +
                "\n\n" +
                mockProblem
                        .getProblemLanguages()
                        .get(0)
                        .getTestRunAll()
                        .replace("results_ENVIRONMENT_VAR.txt", "results_" + request.username() + ".txt");


        MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();

        // When
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeAllTestCases(request, script, mockProblem)).thenReturn(multipleTestCaseResults);

        MultipleTestCaseResults result = sut.runScriptForAllTestCases(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeAllTestCases(request, script, mockProblem);
        assertThat(multipleTestCaseResults).isEqualTo(result);
    }

    @Test
    void runScriptForAllTestCaseWithError(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        String script = request.script() +
                "\n\n" +
                mockProblem
                        .getProblemLanguages()
                        .get(0)
                        .getTestRunAll()
                        .replace("results_ENVIRONMENT_VAR.txt", "results_" + request.username() + ".txt");


        // When
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);

        MultipleTestCaseResults multipleTestCaseResults = mock(MultipleTestCaseResults.class);

        when(mockJavaCompilerService.executeAllTestCases(request, script, mockProblem)).thenReturn(multipleTestCaseResults);

        assertThatThrownBy(() -> sut.runScriptForAllTestCases(request, mockProblem))
                .hasMessageContaining("No serializer found")
                .isInstanceOf(RuntimeException.class);
    }


    @Test
    void runScriptForAllTestCaseWithChallenge(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        String script = request.script() +
                "\n\n" +
                mockProblem
                        .getProblemLanguages()
                        .get(0)
                        .getTestRunAll()
                        .replace("results_ENVIRONMENT_VAR.txt", "results_" + request.username() + ".txt");


        MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();

        // When
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeAllTestCases(request, script, mockProblem)).thenReturn(multipleTestCaseResults);

        MultipleTestCaseResults result = sut.runScriptForAllTestCasesWithChallenge(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeAllTestCases(request, script, mockProblem);
        assertThat(multipleTestCaseResults).isEqualTo(result);
    }
}
