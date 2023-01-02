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
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Service
@AllArgsConstructor
public class SubmissionService {
    private SubmissionRepository submissionRepository;
    private UserRepo userRepo;

    public void addSubmission(Submission submission, int userId, MultipleTestCaseResults multipleTestCaseResults){
        Optional<User> user = userRepo.findById(userId);
        User foundUser;

        if (user.isEmpty()){
            return;
        }

        foundUser = user.get();
        int originalPoints = foundUser.getProblemPoints();

        // So if the user already exists.
        // Check if the user has already made a submission for this problem
        List<Submission> submissionList = submissionRepository.findSubmissionByUserAndProblem(userId, submission.getProblem().getId());

        // If they have, and they have scored higher this time than we need to add the difference.
        if (!submissionList.isEmpty()){
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
