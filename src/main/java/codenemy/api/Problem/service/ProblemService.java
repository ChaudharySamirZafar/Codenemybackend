package codenemy.api.Problem.service;

import codenemy.api.Problem.repository.ProblemRepository;
import codenemy.api.Problem.model.Problem;
import codenemy.api.Submission.Repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Set<Integer> integerSet = submissionRepository.findSubmissionByUserAndProblem(userId);

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
