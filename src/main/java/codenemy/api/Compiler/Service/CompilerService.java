package codenemy.api.Compiler.Service;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
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
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class CompilerService {
    private CompilerServiceFactory compilerServiceFactory;
    private SubmissionService submissionService;
    private ObjectMapper objectMapper;

    /**
     * This method run's when the user attempts to run the code.
     */
    public SingleTestCaseResult runScriptForOneTestCase(Request request, Problem problem) {

        LanguageCompilerServiceIF languageCompilerServiceIF = compilerServiceFactory.getSpecificLanguageCompilerService(request.language());

        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        return languageCompilerServiceIF.executeSingleTestCase(request, problem, problemLanguage);
    }

    /**
     * This method run's when the user attempts to submit the code.
     */
    public MultipleTestCaseResults runScriptForAllTestCases(Request request, Problem problem) {

        LanguageCompilerServiceIF languageCompilerServiceIF = compilerServiceFactory.getSpecificLanguageCompilerService(request.language());

        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        MultipleTestCaseResults multipleTestCaseResults =
                languageCompilerServiceIF.executeAllTestCases(request, problem, problemLanguage);
        String json;

        // If the code contains error then compose the JSON to show there is errors.
        if (multipleTestCaseResults.getError() == null) {
            try {
                json = objectMapper.writeValueAsString(multipleTestCaseResults);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            json = "Code did not compile";
        }

        // Adding the submission to the database
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

    /**
     * This method is run when the user attempts to submit their code for a problem whilst they are challenging other users.
     */
    public MultipleTestCaseResults runScriptForAllTestCasesWithChallenge(Request request, Problem problem) {

        LanguageCompilerServiceIF languageCompilerServiceIF = compilerServiceFactory.getSpecificLanguageCompilerService(request.language());

        ProblemLanguage problemLanguage = findProblemLanguageSelected(problem, request.language());

        return languageCompilerServiceIF.executeAllTestCases(request, problem, problemLanguage);
    }

    /**
     * This method extracts the Language data that is relevant to the language that the user is using.
     * The language data includes the base code for the problem, the code to insert to run one testcase and three.
     * @param problem - The problem the user is trying to run their code against
     * @param selectedLang - The programming lang the user is using
     * @return the correct ProblemLanguage
     */
    private ProblemLanguage findProblemLanguageSelected(Problem problem, String selectedLang){

        return problem.getProblemLanguages()
                        .stream()
                        .filter(element -> element.getLanguage().getProgrammingLanguage().equalsIgnoreCase(selectedLang))
                        .findAny()
                        .get();
    }
}
