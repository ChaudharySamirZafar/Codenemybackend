package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.*;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.ProblemLanguage;
import codenemy.api.Problem.model.TestCase;
import codenemy.api.Util.CompilerUtility;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import lombok.AllArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@AllArgsConstructor
public class PythonCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;
    private static final String PYTHON_VERSION = "3.10.0";

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
