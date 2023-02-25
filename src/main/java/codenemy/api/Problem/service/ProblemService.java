package codenemy.api.Problem.service;

import codenemy.api.Problem.model.Problem;
import codenemy.api.Problem.repository.ProblemRepository;
import codenemy.api.Submission.Repository.SubmissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    public List<Problem> getAllProblems(){

        return problemRepository.findAll();
    }

    /**
     * A variation of getAllProblems, this variation takes into account the current user logged in.
     * It finds all submissions made by the user, so, if a problem has been solved by a user then the solved flag
     * on the problem is set.
     * @param userId the id of the user trying to retrieve a list of problems
     * @return a list of problems
     */
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
