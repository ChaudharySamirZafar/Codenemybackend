package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.ProblemLanguage;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public interface LanguageCompilerServiceIF {

    /**
     * This method run's when the user attempts to run the code.
     */
    SingleTestCaseResult executeSingleTestCase(Request request, Problem problem, ProblemLanguage problemLanguage);

    /**
     * This method run's when the user attempts to submit the code
     * or when the user attempts to submit their code for a problem whilst they are challenging other users
     */
    MultipleTestCaseResults executeAllTestCases(Request request, Problem problem, ProblemLanguage problemLanguage);
}
