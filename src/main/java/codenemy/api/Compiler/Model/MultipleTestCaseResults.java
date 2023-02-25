package codenemy.api.Compiler.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;

/**
 * This class is a DTO, Used to inform the client what percentage they got and how many points.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class MultipleTestCaseResults {
    private List<SingleTestCaseResult> testCaseResultList;
    private int percentage;
    private int points;
    private int newProblemPoints;
    @Transient
    private List<String> error;
}
