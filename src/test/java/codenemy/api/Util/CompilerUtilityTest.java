package codenemy.api.Util;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
public class CompilerUtilityTest {
    CompilerUtility sut;

    @BeforeEach
    void setUp(){
        sut = new CompilerUtility();
    }

    @Test
    void startProcessWithError() {
        sut.startProcess("java --version");
    }

    @Test
    void startProcessWithOutError() {
        Process result = sut.startProcess("testingWithStupidProcess");

        assertNull(result);
    }

    @Test
    void createAndDeleteNewFileWithoutError(){
        // Given
        String testFileName = "test.txt";

        // When
        sut.createNewFile(testFileName);

        // Then
        File file = new File(testFileName);
        assertNotNull(file);
        assertEquals(testFileName, file.getName());

        file.delete();
    }

    @Test
    void createNewFileWithError(){
        // Given
        String invalidFileName = "foo.exe\0. png";

        // When
        sut.createNewFile(invalidFileName);

        // Then
        File file = new File(invalidFileName);
        assertFalse(file.exists());
    }

    @Test
    void writeScriptToFileWithNoError() throws FileNotFoundException {
        // Given
        String script = "Hello World";
        sut.createNewFile("testtest.txt");

        // When
        sut.writeScriptToFile(script);

        // Then
        File file = new File("testtest.txt");
        Scanner scanner = new Scanner(file);

        String output = scanner.nextLine();
        assertEquals(output, script);

        scanner.close();
        file.delete();
    }

    @Test
    void writeScriptToFileWithError() throws IOException {
        // Given
        String script = "Hello World";
        sut.createNewFile("test.txt");
        File file = new File("test.txt");

        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();

        // Use the file channel to create a lock on the file.
        // This method blocks until it can retrieve the lock.
        FileLock lock = channel.lock();

        // When
        sut.writeScriptToFile(script);

        lock.release();
        file.delete();
    }

    @Test
    void deleteFile() throws IOException {
        // Given
        File file = new File("testv1.txt");
        file.createNewFile();

        // when
        sut.deleteFile(file.getName());

        // then
        assertFalse(file.exists());
    }

    @Test
    void doNotDeleteFile() throws IOException {
        // Given
        File file = new File("testv12.txt");
        file.createNewFile();

        // when
        sut.deleteFile("testNotToDelete.txt");

        // then
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    void retrieveTestCaseResult(){
        // Given
        Request request =
                new Request("java", "", 0, "samirzafartest", 0);

        Process process = sut.startProcess("java TestFile.java");

        // When
        TestCaseResult result = sut.retrieveTestCaseResult(request, process);

        // Then
        assertEquals(1, result.getResult().size());
        assertEquals("[0,1]", result.getResult().get(0));

        assertEquals(1, result.getOutput().size());
        assertEquals("Hello World", result.getOutput().get(0));
    }

    @Test
    void retrieveTestCaseResultFailingResult(){
        // Given
        Request request =
                new Request("java", "", 0, "samirzafartest fail", 0);

        Process process = sut.startProcess("python --version");

        // When & Then
        assertThatThrownBy(() -> sut.retrieveTestCaseResult(request, process))
                .hasMessageContaining("java.io.FileNotFoundException")
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void retrieveTestCaseResultFailingOutput() throws IOException {
        // Given
        Request request =
                new Request("java", "", 0, "samirzafartest", 0);

        Process process = sut.startProcess("java TestFile.java");

        process.getInputStream().close();

        // When & Then
        sut.retrieveTestCaseResult(request, process);

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
}
