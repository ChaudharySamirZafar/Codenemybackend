package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.TestCase;

import java.util.Comparator;

public class JavaCompilerService implements LanguageCompilerServiceIF {
    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, String script, Problem problem) {

        compilerUtil.createNewFile("TestRun.java");
        compilerUtil.writeScriptToFile(script);

        compilerUtil.startProcess("javac TestRun.java");
        Process process  = compilerUtil.startProcess("java TestRun");

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));

        return compilerUtil.calculateSingleTestResultWithResponse(problem, compilerUtil.retrieveTestCaseResult(request, process));
    }

    @Override
    public MultipleTestCaseResults executeAllTestCases(Request request, String script, Problem problem) {

        compilerUtil.createNewFile("TestRun.java");
        compilerUtil.writeScriptToFile(script);

        compilerUtil.startProcess("javac TestRun.java");
        Process process  = compilerUtil.startProcess("java TestRun");

        problem.getTestCases().sort(Comparator.comparing(TestCase::getProblemId));

        return compilerUtil.calculateAllTestResultsWithResponse(problem, compilerUtil.retrieveTestCaseResult(request, process));
    }
}
