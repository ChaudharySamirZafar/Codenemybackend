package codenemy.api.Compiler.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Getter
@Setter
@NoArgsConstructor
public class MultipleTestCaseResults {
    private List<SingleTestCaseResult> testCaseResultList;
    private int percentage;
    private int points;
    private int newProblemPoints;
}
