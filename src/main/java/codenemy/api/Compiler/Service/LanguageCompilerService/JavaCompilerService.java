package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;
import codenemy.api.Util.CompilerUtility;
import lombok.AllArgsConstructor;

import java.io.File;
import java.util.Comparator;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@AllArgsConstructor
public class JavaCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;

    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, String script, Problem problem) {

        script = alterScript(script, request.username());

        TestCaseResult[] testCaseResults = createNewFile(request, script, problem);

        SingleTestCaseResult singleTestCaseResult =
                new SingleTestCaseResult(null, null, null, null, false, testCaseResults[0].getError());

        if (!(testCaseResults[0].getError().size() > 0) && !(testCaseResults[1].getError().size() > 0)) {
            singleTestCaseResult =
                    compilerUtil.calculateSingleTestResultWithResponse(problem, testCaseResults[1]);
        } else {
            if (!(testCaseResults[0].getError().size() > 0)) {
                singleTestCaseResult.error().clear();
                singleTestCaseResult.error().addAll(testCaseResults[1].getError());
            }
        }

        compilerUtil.deleteFile("TestRun_"+request.username()+".class");
        compilerUtil.deleteFile("TestRun_"+request.username()+".java");
        compilerUtil.deleteFile("Solution_"+request.username()+".class");
        compilerUtil.deleteFile("results_"+request.username()+".txt");

        return singleTestCaseResult;
    }

    @Override
    public MultipleTestCaseResults executeAllTestCases(Request request, String script, Problem problem) {
        script = alterScript(script, request.username());

        TestCaseResult[] testCaseResults = createNewFile(request, script, problem);

        MultipleTestCaseResults multipleTestCaseResults =
                new MultipleTestCaseResults();
        multipleTestCaseResults.setError(testCaseResults[0].getError());

        if (!(testCaseResults[0].getError().size() > 0) && !(testCaseResults[1].getError().size() > 0)) {
            multipleTestCaseResults =
                    compilerUtil.calculateAllTestResultsWithResponse(problem, testCaseResults[1]);
        } else {
            if (!(testCaseResults[0].getError().size() > 0)) {
                multipleTestCaseResults.getError().clear();
                multipleTestCaseResults.getError().addAll(testCaseResults[1].getError());
            }
        }

        compilerUtil.deleteFile("TestRun_"+request.username()+".class");
        compilerUtil.deleteFile("TestRun_"+request.username()+".java");
        compilerUtil.deleteFile("Solution_"+request.username()+".class");
        compilerUtil.deleteFile("results_"+request.username()+".txt");

        return multipleTestCaseResults;
    }

    private TestCaseResult[] createNewFile(Request request, String script, Problem problem) {

        File file = compilerUtil.createNewFile("TestRun_"+request.username()+".java");
        compilerUtil.writeScriptToFile(script, file);

        Process compileProcess = compilerUtil.startProcess("javac TestRun_"+request.username()+".java");
        Process process  = compilerUtil.startProcess("java TestRun_"+request.username());

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));

        TestCaseResult compileResult = compilerUtil.retrieveTestCaseResult(request, compileProcess);
        TestCaseResult testCaseResult = compilerUtil.retrieveTestCaseResult(request, process);

        return new TestCaseResult[]{compileResult, testCaseResult};
    }

    private String alterScript(String script, String username) {
        return script
                .replace("public class TestRun", "public class TestRun_" + username)
                .replace("Solution", "Solution_"+username);
    }
}
