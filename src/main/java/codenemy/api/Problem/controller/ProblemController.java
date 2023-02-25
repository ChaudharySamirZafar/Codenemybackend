package codenemy.api.Problem.controller;

import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.service.ProblemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/problem")
@AllArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Problem>> getAllProblems(){

        return ResponseEntity.ok().body(problemService.getAllProblems());
    }

    @GetMapping(path = "/getAllProblemsLoggedIn")
    public ResponseEntity<List<Problem>> getAllProblemsWithSolvedInformation(@RequestParam int userId){

        return ResponseEntity.ok().body(problemService.getAllProblemsLoggedIn(userId));
    }

    @GetMapping(path = "/getProblem")
    public ResponseEntity<Problem> getProblem(@RequestParam int id){

        return ResponseEntity.ok().body(problemService.getProblem(id));
    }

    @GetMapping(path = "/getCount")
    public ResponseEntity<Integer> getProblemCount(){

        return ResponseEntity.ok().body(problemService.getAllProblems().size());
    }
}
