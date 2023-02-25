package codenemy.api.Compiler.Service;

import codenemy.api.Auth.model.User;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Service.LanguageCompilerService.JavaCompilerService;
import codenemy.api.Problem.model.*;
import codenemy.api.Submission.Model.Submission;
import codenemy.api.Submission.Service.SubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@ComponentScan("codenemy.api.Problem.repository")
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class CompilerServiceTest {
    private CompilerService sut;
    @Mock
    private CompilerServiceFactory mockCompilerServiceFactory;
    @Mock
    private SubmissionService mockSubmissionService;
    private Problem mockProblem;
    @Mock
    private JavaCompilerService mockJavaCompilerService;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){

        sut = new CompilerService(mockCompilerServiceFactory, mockSubmissionService, objectMapper);

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
    }

    @Test
    void runScriptForOneTestCase(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        SingleTestCaseResult singleTestCaseResult = new SingleTestCaseResult("[5]", "10", null, null, false, null);

        // When
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeSingleTestCase(request, mockProblem, mockProblem.getProblemLanguages().get(0))).thenReturn(singleTestCaseResult);

        sut.runScriptForOneTestCase(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeSingleTestCase(request, mockProblem, mockProblem.getProblemLanguages().get(0));
    }

    @Test
    void runScriptForAllTestCase(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();

        // When
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0))).thenReturn(multipleTestCaseResults);

        sut.runScriptForAllTestCases(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0));
        verify(mockSubmissionService).addSubmission(any(Submission.class), any(Integer.class), any(MultipleTestCaseResults.class));
    }

    @Test
    void runScriptForAllTestCaseWithError(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();

        // When
        multipleTestCaseResults.setError(Arrays.asList("Error", "Error"));
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0))).thenReturn(multipleTestCaseResults);

        sut.runScriptForAllTestCases(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0));
    }

    @Test
    void runScriptForAllTestCaseWithJsonError() {

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);
        MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();

        // When
        try {
            when(objectMapper.writeValueAsString(multipleTestCaseResults)).thenThrow(JsonProcessingException.class);
        } catch (Exception e) {
            assertEquals(e, instanceOf(RuntimeException.class));
        }
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0))).thenReturn(multipleTestCaseResults);

        // Then
        assertThatThrownBy(() ->  sut.runScriptForAllTestCases(request, mockProblem))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void runScriptForAllTestCaseChallenge(){

        // Given
        Request request = new Request("java", "import java.util.Arrays;\\r\\nimport java.io.*;\\r\\n\\r\\nclass Solution {\\r\\n    public static int[] twoSum(int[] nums, int target) {\\r\\n       System.out.println(\\\"Testing\\\");\\r\\n       System.out.println(\\\"Testing Testing\\\");\\r\\n       System.out.println(\\\"Testing Testingv2\\\");\\r\\n       System.out.println(\\\"Testing Testingv3\\\");\\r\\n       return new int[]{0, 0};\\r\\n    }\\r\\n}",
                1, "samirzafar", 1);

        MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();

        // When
        multipleTestCaseResults.setError(Arrays.asList("Error", "Error"));
        when(mockCompilerServiceFactory.getSpecificLanguageCompilerService("java")).thenReturn(mockJavaCompilerService);
        when(mockJavaCompilerService.executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0))).thenReturn(multipleTestCaseResults);

        sut.runScriptForAllTestCasesWithChallenge(request, mockProblem);

        // Then
        verify(mockCompilerServiceFactory).getSpecificLanguageCompilerService("java");
        verify(mockJavaCompilerService).executeAllTestCases(request, mockProblem, mockProblem.getProblemLanguages().get(0));
    }
}
