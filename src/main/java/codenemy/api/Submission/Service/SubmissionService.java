package codenemy.api.Submission.Service;

import codenemy.api.Auth.model.User;
import codenemy.api.Auth.repository.UserRepo;
import codenemy.api.Compiler.Model.MultipleTestCaseResults;
import codenemy.api.Submission.Model.Submission;
import codenemy.api.Submission.Repository.SubmissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class SubmissionService {
    private SubmissionRepository submissionRepository;
    private UserRepo userRepo;

    /**
     * A method that adds a submission to the database
     * @param submission - the submission by the user
     * @param userId - the user that is making the submission
     * @param multipleTestCaseResults - the results of every test case for the problem the user is submitting
     */
    public void addSubmission(Submission submission, int userId, MultipleTestCaseResults multipleTestCaseResults){

        Optional<User> user = userRepo.findById(userId);
        User foundUser;

        if (user.isEmpty()){
            return;
        }

        foundUser = user.get();

        // Take note of the amount of points the user already has.
        int originalPoints = foundUser.getProblemPoints();

        // Check if the user has already made a submission for this problem.
        List<Submission> submissionList = submissionRepository.findSubmissionByUserAndProblem(userId, submission.getProblem().getId());

        // If they have made a submission already, and they have scored higher this time than we need to add the difference to their points.
        if (!submissionList.isEmpty()){
            // We get the element that has the most points, so we know their previous highest score for the submission of this problem.
            int difference = submission.getPoints() - submissionList.get(submissionList.size() - 1).getPoints();
            if (difference > 0){
                foundUser.setProblemPoints(foundUser.getProblemPoints() + difference);
            }
        } else {
            foundUser.setProblemPoints(foundUser.getProblemPoints() + submission.getPoints());
        }

        if (originalPoints != foundUser.getProblemPoints()){
            userRepo.save(foundUser);
        }

        multipleTestCaseResults.setNewProblemPoints(foundUser.getProblemPoints());

        submission.setUser(foundUser);
        submissionRepository.save(submission);
    }
}
