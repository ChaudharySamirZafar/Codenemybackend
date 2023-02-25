package codenemy.api.Compiler.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used internally. It's primary purpose is to get the code output, result and errors.
 * After this object is constructed it will be used to make a SingleTestCaseResult or MultipleTestCaseResults object.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class TestCaseResult {
    private int id;
    private List<String> output;
    private List<String> result;
    private List<String> error;

    public TestCaseResult(int id, List<String> result) {
        this.id = id;
        output = new ArrayList<>();
        this.result = result;
        error = new ArrayList<>();
    }
}
