package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import codenemy.api.Util.CompilerUtility;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import lombok.AllArgsConstructor;

import java.util.Comparator;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@AllArgsConstructor
public class PythonCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;
    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, String script, Problem problem) {

        compilerUtil.createNewFile("test.py");
        compilerUtil.writeScriptToFile(script);

        Process process = compilerUtil.startProcess("python3 test.py");

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));

        TestCaseResult testCaseResult = compilerUtil.retrieveTestCaseResult(request, process);

        SingleTestCaseResult singleTestCaseResult =
                new SingleTestCaseResult(null, null, null, null, false, testCaseResult.getError());

        if (!(testCaseResult.getError().size() > 0)) {
           singleTestCaseResult =
                   compilerUtil.calculateSingleTestResultWithResponse(problem, testCaseResult);
        }

        compilerUtil.deleteFile("test.py");
        compilerUtil.deleteFile("results_"+request.username()+".txt");

        return singleTestCaseResult;
    }

    @Override
    public MultipleTestCaseResults executeAllTestCases(Request request, String script, Problem problem) {

        compilerUtil.createNewFile("test.py");
        compilerUtil.writeScriptToFile(script);

        Process process = compilerUtil.startProcess("python test.py");

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));

        TestCaseResult testCaseResult = compilerUtil.retrieveTestCaseResult(request, process);

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));

        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();
        multipleTestCaseResults.setError(testCaseResult.getError());

        if (!(testCaseResult.getError().size() > 0)) {
            multipleTestCaseResults =
                    compilerUtil.calculateAllTestResultsWithResponse(problem, testCaseResult);
        }

        compilerUtil.deleteFile("test.py");
        compilerUtil.deleteFile("results_"+request.username()+".txt");

        return multipleTestCaseResults;
    }
}
