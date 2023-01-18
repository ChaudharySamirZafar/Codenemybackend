package codenemy.api.Problem.service;

import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.repository.ProblemRepository;
import codenemy.api.Submission.Repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 30/12/2022
 */
@Service
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    @Autowired
    public ProblemService(ProblemRepository problemRepository, SubmissionRepository submissionRepository){
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
    }

    public List<Problem> getAllProblems(){
        return problemRepository.findAll();
    }

    public List<Problem> getAllProblemsLoggedIn(int userId){
        Set<Integer> integerSet = submissionRepository.findSubmissionByUser(userId);

        List<Problem> problemList = problemRepository.findAll();

        if (problemList.isEmpty()){
            return null;
        }

        for (Problem problem : problemList) {
            problem.setSolved(integerSet.contains(problem.getId()));
        }

        return problemList;
    }

    public Problem getProblem(int id) {
        Optional<Problem> result = problemRepository.findById(id);

        return result.orElse(null);
    }
}
