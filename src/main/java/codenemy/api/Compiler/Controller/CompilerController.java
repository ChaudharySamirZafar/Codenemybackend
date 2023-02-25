package codenemy.api.Compiler.Controller;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Service.CompilerService;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.service.ProblemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(path = "/api/compiler")
@CrossOrigin
@AllArgsConstructor
public class CompilerController {
    private final ProblemService problemService;
    private final CompilerService compilerService;

    /**
     * This endpoint is hit when the user attempts to run the code.
     */
    @PostMapping(path = "/runSingleTestCase")
    public ResponseEntity<SingleTestCaseResult> runScriptForOneTestCaseVersionTwo(@RequestBody Request request) {

        Problem problem = problemService.getProblem(request.problemId());

        SingleTestCaseResult singleTestCaseResult = compilerService.runScriptForOneTestCase(request, problem);

        return ResponseEntity.ok().body(singleTestCaseResult);
    }

    /**
     * This endpoint is hit when the user attempts to submit their code for a problem.
     */
    @PostMapping(path = "/runAllTestCases")
    public ResponseEntity<MultipleTestCaseResults> runScriptForAllTestCases(@RequestBody Request request) {

        return ResponseEntity.ok().body(compilerService.runScriptForAllTestCases(request, problemService.getProblem(request.problemId())));
    }

    /**
     * This endpoint is hit when the user attempts to submit their code for a problem whilst they are challenging other users.
     */
    @PostMapping(path = "/runAllTestCasesChallenge")
    public ResponseEntity<MultipleTestCaseResults> runScriptForAllTestCasesWithChallenge(@RequestBody Request request) {

        return ResponseEntity.ok().body(compilerService.runScriptForAllTestCasesWithChallenge(request, problemService.getProblem(request.problemId())));
    }
}