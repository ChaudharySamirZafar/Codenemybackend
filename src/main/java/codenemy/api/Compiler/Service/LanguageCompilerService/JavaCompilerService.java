package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.TestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.ProblemLanguage;
import codenemy.api.Util.CompilerUtility;
import lombok.AllArgsConstructor;

import java.util.Scanner;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@AllArgsConstructor
public class JavaCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;
    private static final String JAVA_VERSION = "15.0.2";

    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, Problem problem, ProblemLanguage problemLanguage) {
        CompilerUtility compilerUtil = new CompilerUtility();

        TestCaseResult testCaseResult =
                compilerUtil.getTestCaseResult
                        ("TestRun.java", buildFinalScript(request, problemLanguage.getTestRunOne()), request.language(), JAVA_VERSION);

        if (testCaseResult.getError().size() > 0) {
            return new SingleTestCaseResult(null, null, null, null, false, testCaseResult.getError());
        }

        return compilerUtil.calculateSingleTestResultWithResponse(problem, testCaseResult);
    }

    @Override
    public MultipleTestCaseResults executeAllTestCases(Request request, Problem problem, ProblemLanguage problemLanguage) {
        CompilerUtility compilerUtil = new CompilerUtility();

        TestCaseResult testCaseResult =
                compilerUtil.getTestCaseResult
                        ("TestRun.java", buildFinalScript(request, problemLanguage.getTestRunAll()), request.language(), JAVA_VERSION);

        if (testCaseResult.getError().size() > 0) {
           MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();
           multipleTestCaseResults.setError(testCaseResult.getError());
           return multipleTestCaseResults;
        }

        return compilerUtil.calculateAllTestResultsWithResponse(problem, testCaseResult);
    }

    private String buildFinalScript(Request request, String testRunOne) {
        Scanner scanner = new Scanner(request.script());
        StringBuilder importStatements = new StringBuilder();
        StringBuilder code = new StringBuilder();

        while (scanner.hasNext()){
            String newLine = scanner.nextLine();
            if (newLine.startsWith("import")) {
                importStatements.append(newLine).append("\r\n");
            } else {
                code.append(newLine).append("\r\n");
            }
        }

        scanner.close();

        return importStatements + testRunOne + code;
    }


}