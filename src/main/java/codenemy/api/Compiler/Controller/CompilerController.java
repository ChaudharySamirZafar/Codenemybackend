package codenemy.api.Compiler.Controller;

import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Compiler.Model.Request;
import codenemy.api.Compiler.Model.SingleTestCaseResult;
import codenemy.api.Compiler.Service.CompilerService;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executor;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@RestController
@RequestMapping(path = "/api/compiler")
@CrossOrigin
@EnableAsync
public class CompilerController {
    private final ProblemService problemService;
    private final CompilerService compilerService;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Code Compilation - ");
        executor.initialize();
        return executor;
    }

    @Autowired
    public CompilerController(ProblemService problemService, CompilerService compilerService){
        this.problemService = problemService;
        this.compilerService = compilerService;
    }
    
    @PostMapping(path = "/runAllTestCases")
    @Async
    public ResponseEntity<MultipleTestCaseResults> runScriptForAllTestCases(@RequestBody Request request) {

        return ResponseEntity.ok().body(compilerService.runScriptForAllTestCases(request, problemService.getProblem(request.problemId())));
    }

    @PostMapping(path = "/runAllTestCasesChallenge")
    @Async
    public ResponseEntity<MultipleTestCaseResults> runScriptForAllTestCasesWithChallenge(@RequestBody Request request) {

        return ResponseEntity.ok().body(compilerService.runScriptForAllTestCasesWithChallenge(request, problemService.getProblem(request.problemId())));
    }

    @PostMapping(path = "/runSingleTestCase")
    @Async
    public ResponseEntity<SingleTestCaseResult> runScriptForOneTestCaseVersionTwo(@RequestBody Request request) {

        Problem problem = problemService.getProblem(request.problemId());

        SingleTestCaseResult singleTestCaseResult = compilerService.runScriptForOneTestCase(request, problem);

        return ResponseEntity.ok().body(singleTestCaseResult);
    }
}