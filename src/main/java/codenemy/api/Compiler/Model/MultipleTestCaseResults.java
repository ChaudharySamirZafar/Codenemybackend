package codenemy.api.Compiler.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MultipleTestCaseResults {
    private List<SingleTestCaseResult> testCaseResultList;
    private int percentage;
    private int points;
}
