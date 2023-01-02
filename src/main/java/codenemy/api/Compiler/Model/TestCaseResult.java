package codenemy.api.Compiler.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Getter
@Setter
public class TestCaseResult {
    private int id;
    private List<String> output;
    private List<String> result;

    public TestCaseResult(int id, List<String> result) {
        this.id = id;
        output = new ArrayList<>();
        this.result = result;
    }
}
