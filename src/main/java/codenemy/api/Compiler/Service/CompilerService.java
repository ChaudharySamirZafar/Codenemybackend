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
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Service
@AllArgsConstructor
public class CompilerService {
    private CompilerServiceFactory compilerServiceFactory;
    private SubmissionService submissionService;

    public SingleTestCaseResult runScriptForOneTestCaseVersionOne(Request request, Problem problem) {
        // Retrieve the correct and appropriate compiler service
        LanguageCompilerServiceIF languageCompilerServiceIF = compilerServiceFactory.getSpecificLanguageCompilerService(request.language());

        // First find the problem Lang
        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        return languageCompilerServiceIF.executeSingleTestCase(request, problem, problemLanguage);
    }

    public MultipleTestCaseResults runScriptForAllTestCases(Request request, Problem problem) {
        // Retrieve the correct and appropriate compiler service
        LanguageCompilerServiceIF languageCompilerServiceIF = compilerServiceFactory.getSpecificLanguageCompilerService(request.language());

        // First find the problem Lang
        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        MultipleTestCaseResults multipleTestCaseResults =
                languageCompilerServiceIF.executeAllTestCases(request, problem, problemLanguage);
        String json;

        if (multipleTestCaseResults.getError() == null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                json = mapper.writeValueAsString(multipleTestCaseResults);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            json = "Code did not compile";
        }

        submissionService.addSubmission(
                new Submission(0,
                        null,
                        problem,
                        LocalDateTime.now(),
                        json,
                        multipleTestCaseResults.getPercentage(),
                        multipleTestCaseResults.getPoints()),
                request.userId(),
                multipleTestCaseResults);

        return multipleTestCaseResults;
    }

    public MultipleTestCaseResults runScriptForAllTestCasesWithChallenge(Request request, Problem problem) {
        // Retrieve the correct and appropriate compiler service
        LanguageCompilerServiceIF languageCompilerServiceIF = compilerServiceFactory.getSpecificLanguageCompilerService(request.language());

        // First find the problem Lang
        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        return languageCompilerServiceIF.executeAllTestCases(request, problem, problemLanguage);
    }

    private ProblemLanguage findProblemLanguageSelected(Problem problem, String selectedLang){
        return problem.getProblemLanguages()
                        .stream()
                        .filter(element -> element.getLanguage().getProgrammingLanguage().equalsIgnoreCase(selectedLang))
                        .findAny()
                        .get();
    }
}
