package codenemy.api.Compiler.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
public class TestCaseResultTest {
    TestCaseResult sut;

    @BeforeEach
    void setUp(){
        sut = new TestCaseResult(0, null);
    }

    @Test
    void testCaseResultGettersAndSetters(){
        // Given
        int newId = 500;
        List<String> newResults = List.of("1","2","3");

        // When
        sut.setId(newId);
        sut.setResult(newResults);

        // Then
        assertEquals(newId, sut.getId());
        assertEquals(newResults, sut.getResult());
    }
}
