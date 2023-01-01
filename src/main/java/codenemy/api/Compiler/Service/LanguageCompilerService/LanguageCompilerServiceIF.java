package codenemy.api.Compiler.Service.LanguageCompilerService;

import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Util.CompilerUtility;

public interface LanguageCompilerServiceIF {
    public SingleTestCaseResult executeSingleTestCase(Request request, String script, Problem problem);
    public MultipleTestCaseResults executeAllTestCases(Request request, String script, Problem problem);
    public CompilerUtility compilerUtil = new CompilerUtility();
}
