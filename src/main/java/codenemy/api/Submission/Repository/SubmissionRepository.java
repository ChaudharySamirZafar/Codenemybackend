package codenemy.api.Submission.Repository;

import codenemy.api.Submission.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    /**
     * A method that finds all submissions made by a specific user
     * @param userId that is used to filter the search
     * @return A list of problem id's that the user has submitted
     */
    @Query("select s.problem.id from Submission s where s.user.id = :userId and s.percentage = 100")
    Set<Integer> findSubmissionByUser(@Param("userId") int userId);

    /**
     * A method that finds all submissions made by a specific user on a specific problem
     * @param userId that is used to filter the search
     * @param problemId that is used to filter the search
     * @return A list of Submission models
     */
    @Query("select s from Submission s where s.user.id = :userId and s.problem.id = :problemId order by s.points")
    List<Submission> findSubmissionByUserAndProblem(@Param("userId") int userId, @Param("problemId") int problemId);
}
