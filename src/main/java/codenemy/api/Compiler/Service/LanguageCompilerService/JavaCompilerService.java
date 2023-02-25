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
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
public class JavaCompilerService implements LanguageCompilerServiceIF {
    CompilerUtility compilerUtil;
    private static final String JAVA_VERSION = "15.0.2";

    /**
     * This method run's when the user attempts to run the code.
     */
    @Override
    public SingleTestCaseResult executeSingleTestCase(Request request, Problem problem, ProblemLanguage problemLanguage) {

        TestCaseResult testCaseResult =
                compilerUtil.getTestCaseResult
                        ("TestRun.java", buildFinalScript(request, problemLanguage.getTestRunOne()), request.language(), JAVA_VERSION);

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
                        ("TestRun.java", buildFinalScript(request, problemLanguage.getTestRunAll()), request.language(), JAVA_VERSION);

        if (testCaseResult.getError().size() > 0) {
           MultipleTestCaseResults multipleTestCaseResults = new MultipleTestCaseResults();
           multipleTestCaseResults.setError(testCaseResult.getError());
           return multipleTestCaseResults;
        }

        return compilerUtil.calculateAllTestResultsWithResponse(problem, testCaseResult);
    }

    /**
     * This method builds the Java file that will be sent to the Piston API
     * It doesn't accept the Java file if the code isn't in a certain order
     * So, import statements will go at the top, then the code that runs the code the user submits,
     * Finally the code the user has written
     * @param request - used to extract the code that has been written by the user
     * @param mainClassCode - code to insert into file to run it
     * @return finalised script
     */
    private String buildFinalScript(Request request, String mainClassCode) {

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

        return importStatements + mainClassCode + code;
    }


}
