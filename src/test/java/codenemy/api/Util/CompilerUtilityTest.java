package codenemy.api.Util;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public class CompilerUtilityTest {
    CompilerUtility sut;

    @BeforeEach
    void setUp(){

        sut = new CompilerUtility();
    }

    @Test
    void calculateSingleTestResultPass(){

        // Given
        TestCaseResult testCaseResult = new TestCaseResult(0, new ArrayList<String>());
        testCaseResult.getResult().add("[0,1]");

        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "easy", false);

        TestCase testCase = new TestCase(0, "", "[0,1]", 0, null);
        problem.getTestCases().add(testCase);

        // When
        SingleTestCaseResult result = sut.calculateSingleTestResultWithResponse(problem, testCaseResult);

        // Then
        assertTrue(result.pass());
        assertEquals(result.expectedOutput(), result.userOutput());
    }

    @Test
    void calculateSingleTestResultFail(){

        // Given
        TestCaseResult testCaseResult = new TestCaseResult(0, new ArrayList<String>());
        testCaseResult.getResult().add("[0,4]");

        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "easy", false);

        TestCase testCase = new TestCase(0, "", "[0,1]", 0, null);
        problem.getTestCases().add(testCase);

        // When
        SingleTestCaseResult result = sut.calculateSingleTestResultWithResponse(problem, testCaseResult);

        // Then
        assertFalse(result.pass());
    }

    @Test
    void calculateAllTestResultPass(){

        // Given
        TestCaseResult testCaseResult = new TestCaseResult(0, new ArrayList<String>());
        testCaseResult.getResult().add("[0,1]");
        testCaseResult.getResult().add("[0,2]");
        testCaseResult.getResult().add("[0,3]");

        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "easy", false);

        TestCase testCase = new TestCase(0, "", "[0,1]", 0, null);
        problem.getTestCases().add(testCase);

        testCase = new TestCase(0, "", "[0,2]", 0, null);
        problem.getTestCases().add(testCase);

        testCase = new TestCase(0, "", "[0,4]", 0, null);
        problem.getTestCases().add(testCase);

        // When
        MultipleTestCaseResults result = sut.calculateAllTestResultsWithResponse(problem, testCaseResult);

        // Then
        assertTrue(result.getTestCaseResultList().get(0).pass());
        assertTrue(result.getTestCaseResultList().get(1).pass());
        assertFalse(result.getTestCaseResultList().get(2).pass());
        assertEquals(3, result.getTestCaseResultList().size());
        assertEquals(66, result.getPercentage());
        assertEquals(660, result.getPoints());
    }

    @Test
    void calculateAllTestResultMediumPass(){

        // Given
        TestCaseResult testCaseResult = new TestCaseResult(0, new ArrayList<String>());
        testCaseResult.getResult().add("[0,1]");
        testCaseResult.getResult().add("[0,2]");
        testCaseResult.getResult().add("[0,3]");

        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "medium", false);

        TestCase testCase = new TestCase(0, "", "[0,1]", 0, null);
        problem.getTestCases().add(testCase);

        testCase = new TestCase(0, "", "[0,2]", 0, null);
        problem.getTestCases().add(testCase);

        testCase = new TestCase(0, "", "[0,4]", 0, null);
        problem.getTestCases().add(testCase);

        // When
        MultipleTestCaseResults result = sut.calculateAllTestResultsWithResponse(problem, testCaseResult);

        // Then
        assertTrue(result.getTestCaseResultList().get(0).pass());
        assertTrue(result.getTestCaseResultList().get(1).pass());
        assertFalse(result.getTestCaseResultList().get(2).pass());
        assertEquals(3, result.getTestCaseResultList().size());
        assertEquals(66, result.getPercentage());
        assertEquals(1320, result.getPoints());
    }

    @Test
    void calculateAllTestResultHardPass(){

        // Given
        TestCaseResult testCaseResult = new TestCaseResult(0, new ArrayList<String>());
        testCaseResult.getResult().add("[0,1]");
        testCaseResult.getResult().add("[0,2]");
        testCaseResult.getResult().add("[0,3]");

        Problem problem =
                new Problem(0, "", "", null, new ArrayList<TestCase>(), null, "hard", false);

        TestCase testCase = new TestCase(0, "", "[0,1]", 0, null);
        problem.getTestCases().add(testCase);

        testCase = new TestCase(0, "", "[0,2]", 0, null);
        problem.getTestCases().add(testCase);

        testCase = new TestCase(0, "", "[0,4]", 0, null);
        problem.getTestCases().add(testCase);

        // When
        MultipleTestCaseResults result = sut.calculateAllTestResultsWithResponse(problem, testCaseResult);

        // Then
        assertTrue(result.getTestCaseResultList().get(0).pass());
        assertTrue(result.getTestCaseResultList().get(1).pass());
        assertFalse(result.getTestCaseResultList().get(2).pass());
        assertEquals(3, result.getTestCaseResultList().size());
        assertEquals(66, result.getPercentage());
        assertEquals(1980, result.getPoints());
    }

    @Test
    void getTestCaseResultWithError(){

        // Given
        String fileName = "TestRun.py";
        String fileContent = "class Solution {\\r\\n    twoSum(nums, target){\\r\\n      let indexes = [];\\r\\n  \\r\\n      for(let i = 0; i < nums.length; i++){\\r\\n           for(let j = i + 1; j < nums.length; j++){\\r\\n              if (nums[i] + nums[j] === target) {\\r\\n            indexes.push(i);\\r\\n            indexes.push(j);\\r\\n              }\\r\\n           }\\r\\n      }\\r\\n    \\r\\n      return indexes;\\r\\n    }\\r\\n}\\r\\n\\r\\nsolution = new Solution();\\r\\nnums = [2, 7, 11, 15]\\r\\ntarget = 9\\r\\nconsole.log(\\\"\\\\nResult - [\\\" + solution.twoSum(nums, target) + \\\"]\\\");";
        String programmingLang = "java";
        String version = "15.0.2";

        // When
        TestCaseResult testCaseResult = sut.getTestCaseResult(fileName, fileContent, programmingLang, version);

        // Then
        assertTrue(testCaseResult.getError().size() > 0);
    }

    @Test
    void getTestCaseResult(){

        // Given
        String expectedOutput = "Sum of x+y = 35\n";
        String expectedResult = "35";
        String fileName = "TestRun.java";
        String fileContent = "public class MyClass {\n    public static void main(String args[]) {\n      int x=10;\n      int y=25;\n      int z=x+y;\n\n      System.out.println(\"Sum of x+y = \" + z);\n      System.out.println(\"Result - \" + z);\n    }\n}";
        String programmingLang = "java";
        String version = "15.0.2";

        // When
        TestCaseResult testCaseResult = sut.getTestCaseResult(fileName, fileContent, programmingLang, version);

        // Then
        assertEquals(1, testCaseResult.getOutput().size());
        assertEquals(1, testCaseResult.getResult().size());
        assertEquals(expectedOutput, testCaseResult.getOutput().get(0));
        assertEquals(expectedResult, testCaseResult.getResult().get(0));
    }
}
