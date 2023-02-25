package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.ProblemLanguage;
import codenemy.api.Util.CompilerUtility;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JavaScriptCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;
    private static final String JAVASCRIPT_VERSION = "16.3.0";

    /**
     * This method run's when the user attempts to run the code.
     */
    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, Problem problem, ProblemLanguage problemLanguage) {

        TestCaseResult testCaseResult =
                compilerUtil.getTestCaseResult
                        ("TestRun.js", request.script() + "\n" + problemLanguage.getTestRunOne(), request.language(), JAVASCRIPT_VERSION);

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
                        ("TestRun.js", request.script() + "\n" + problemLanguage.getTestRunAll(), request.language(), JAVASCRIPT_VERSION);

        if (testCaseResult.getError().size() > 0) {
            MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();
            multipleTestCaseResults.setError(testCaseResult.getError());
            return multipleTestCaseResults;
        }

        return compilerUtil.calculateAllTestResultsWithResponse(problem, testCaseResult);
    }
}
