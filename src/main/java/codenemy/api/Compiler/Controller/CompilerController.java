package codenemy.api.Compiler.Controller;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Service.CompilerService;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@RestController
@RequestMapping(path = "/api/compiler")
@CrossOrigin
public class CompilerController {
    private final ProblemService problemService;
    private final CompilerService compilerService;

    @Autowired
    public CompilerController(ProblemService problemService, CompilerService compilerService){
        this.problemService = problemService;
        this.compilerService = compilerService;
    }

    @PostMapping(path = "/runSingleTestCase")
    public ResponseEntity<SingleTestCaseResult> runScriptForOneTestCase(@RequestBody Request request) {

        Problem problem = problemService.getProblem(request.problemId());

        return ResponseEntity.ok().body(compilerService.runScriptForOneTestCase(request, problem));
    }

    @PostMapping(path = "/runAllTestCases")
    public ResponseEntity<MultipleTestCaseResults> runScriptForAllTestCases(@RequestBody Request request) {

        return ResponseEntity.ok().body(compilerService.runScriptForAllTestCases(request, problemService.getProblem(request.problemId())));
    }

    @PostMapping(path = "/runAllTestCasesChallenge")
    public ResponseEntity<MultipleTestCaseResults> runScriptForAllTestCasesWithChallenge(@RequestBody Request request) {

        return ResponseEntity.ok().body(compilerService.runScriptForAllTestCasesWithChallenge(request, problemService.getProblem(request.problemId())));
    }
}