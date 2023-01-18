package codenemy.api.Problem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
public class TestCaseTest {
    TestCase sut;

    @Test
    void testCaseGettersAndSetters() {
        sut = new TestCase();

        String newOutput = "newOutput";
        String newInput = "newInput";
        int newId = 15;
        Problem newProblem = new Problem();
        int newProblemId = 16;

        sut.setOutput(newOutput);
        sut.setInput(newInput);
        sut.setId(newId);
        sut.setProblem(newProblem);
        sut.setProblemId(newProblemId);

        assertEquals(newOutput, sut.getOutput());
        assertEquals(newInput, sut.getInput());
        assertEquals(newId, sut.getId());
        assertEquals(newProblem, sut.getProblem());
        assertEquals(newProblemId, sut.getProblemId());
    }
}
