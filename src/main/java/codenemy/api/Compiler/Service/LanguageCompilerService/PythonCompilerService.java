package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;

import java.util.Comparator;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
public class PythonCompilerService implements LanguageCompilerServiceIF {

    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, String script, Problem problem) {

        compilerUtil.createNewFile("test.py");
        compilerUtil.writeScriptToFile(script);

        Process process = compilerUtil.startProcess("python test.py");

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));


        SingleTestCaseResult singleTestCaseResult =
                compilerUtil.calculateSingleTestResultWithResponse(problem, compilerUtil.retrieveTestCaseResult(request, process));

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

        MultipleTestCaseResults multipleTestCaseResults =
                compilerUtil.calculateAllTestResultsWithResponse(problem, compilerUtil.retrieveTestCaseResult(request, process));

        compilerUtil.deleteFile("test.py");
        compilerUtil.deleteFile("results_"+request.username()+".txt");

        return multipleTestCaseResults;
    }
}
