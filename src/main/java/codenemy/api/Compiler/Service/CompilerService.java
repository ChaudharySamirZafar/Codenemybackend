package codenemy.api.Compiler.Service;

import codenemy.api.Compiler.Model.*;
import codenemy.api.Compiler.Service.LanguageCompilerService.LanguageCompilerServiceIF;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.model.ProblemLanguage;
import codenemy.api.Submission.Model.Submission;
import codenemy.api.Submission.Service.SubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author samir.zafar
 * @version 1.0
 * @since 12/09/22
 */
@Service
@AllArgsConstructor
public class CompilerService {
    private DelegationService delegationService;
    private SubmissionService submissionService;

    public SingleTestCaseResult runScriptForOneTestCase(Request request, Problem problem) {

        // Retrieve the correct and appropriate compiler service
        LanguageCompilerServiceIF languageCompilerServiceIF = delegationService.getSpecificLanguageCompilerService(request.language());

        // First find the problem Lang
        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        String editedTestScript = editTestScript(request, problemLanguage, true);

        return languageCompilerServiceIF.executeSingleTestCase(request, editedTestScript, problem);
    }

    public MultipleTestCaseResults runScriptForAllTestCases(Request request, Problem problem) {
        // Retrieve the correct and appropriate compiler service
        LanguageCompilerServiceIF languageCompilerServiceIF = delegationService.getSpecificLanguageCompilerService(request.language());

        // First find the problem Lang
        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        MultipleTestCaseResults multipleTestCaseResults =
                languageCompilerServiceIF.executeAllTestCases(request, editTestScript(request, problemLanguage, false), problem);

        ObjectMapper mapper = new ObjectMapper();

        String json;
        try {
            json = mapper.writeValueAsString(multipleTestCaseResults);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        submissionService.addSubmission(
                new Submission(0, null, problem, LocalDateTime.now(), json, multipleTestCaseResults.getPercentage(), multipleTestCaseResults.getPoints()),
                request.userId(),
                multipleTestCaseResults);

        return multipleTestCaseResults;
    }

    private ProblemLanguage findProblemLanguageSelected(Problem problem, String selectedLang){
        return problem.getProblemLanguages()
                        .stream()
                        .filter(element -> element.getLanguage().getProgrammingLanguage().equalsIgnoreCase(selectedLang))
                        .findAny()
                        .get();
    }

    private String editTestScript(Request request, ProblemLanguage problemLanguage, boolean testRunOne){
        return testRunOne
            ?
        request.script() +
            "\n\n" +
            problemLanguage
                .getTestRunOne()
                .replace("results_ENVIRONMENT_VAR.txt", "results_" + request.username() + ".txt")
            :
        request.script() +
            "\n\n" +
            problemLanguage
                .getTestRunAll()
                .replace("results_ENVIRONMENT_VAR.txt", "results_" + request.username() + ".txt");
    }
}
