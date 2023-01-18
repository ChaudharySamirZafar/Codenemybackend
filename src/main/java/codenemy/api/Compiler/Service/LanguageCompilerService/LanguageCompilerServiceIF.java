package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Problem.model.Problem;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
public interface LanguageCompilerServiceIF {
    public SingleTestCaseResult executeSingleTestCase(Request request, String script, Problem problem);
    public MultipleTestCaseResults executeAllTestCases(Request request, String script, Problem problem);
}
