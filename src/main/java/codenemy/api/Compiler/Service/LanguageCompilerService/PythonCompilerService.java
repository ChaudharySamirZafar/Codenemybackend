package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.ProblemLanguage;
import codenemy.api.Util.CompilerUtility;
import lombok.AllArgsConstructor;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
public class PythonCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;
    private static final String PYTHON_VERSION = "3.10.0";

    /**
     * This method run's when the user attempts to run the code.
     */
    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, Problem problem, ProblemLanguage problemLanguage) {

        TestCaseResult testCaseResult =
                compilerUtil.getTestCaseResult
                        ("TestRun.py", request.script() + "\n" + problemLanguage.getTestRunOne(), request.language(), PYTHON_VERSION);

        if (testCaseResult.getError().size() > 0) {
            return new SingleTestCaseResult(null, null, null, null, false, testCaseResult.getError());
        }

        return compilerUtil.calculateSingleTestResultWithResponse(problem, testCaseResult);
    }

    /**
     * This method run's when the user attempts to submit the code
     * or when the user attempts to submit their code for a problem whilst they are challenging other users
     */
    @Override
    public MultipleTestCaseResults executeAllTestCases(Request request, Problem problem, ProblemLanguage problemLanguage) {

        TestCaseResult testCaseResult =
                compilerUtil.getTestCaseResult
                        ("TestRun.py", request.script() + "\n" + problemLanguage.getTestRunAll(), request.language(), PYTHON_VERSION);

        if (testCaseResult.getError().size() > 0) {
            MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();
            multipleTestCaseResults.setError(testCaseResult.getError());
            return multipleTestCaseResults;
        }

        return compilerUtil.calculateAllTestResultsWithResponse(problem, testCaseResult);
    }
}
