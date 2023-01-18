package codenemy.api.Submission.Model;

import codenemy.api.Auth.model.User;
import codenemy.api.Problem.model.Problem;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
public class SubmissionTest {
    Submission sut;

    @Test
    void submissionGettersAndSetters() {
        sut = new Submission();

        Problem problem = new Problem();
        User user = new User();
        int newId = 5;
        LocalDateTime dateTime = LocalDateTime.now();
        String newDetails = "newDetails";
        int newPoints = 5;
        int newPercentage = 15;

        // Set everything
        sut.setProblem(problem);
        sut.setUser(user);
        sut.setId(newId);
        sut.setDate(dateTime);
        sut.setDetails(newDetails);
        sut.setPoints(5);
        sut.setPercentage(15);

        assertEquals(problem, sut.getProblem());
        assertEquals(user, sut.getUser());
        assertEquals(newId, sut.getId());
        assertEquals(dateTime, sut.getDate());
        assertEquals(newPoints, sut.getPoints());
        assertEquals(newPercentage, sut.getPercentage());
        assertEquals(newDetails, sut.getDetails());
    }
}
