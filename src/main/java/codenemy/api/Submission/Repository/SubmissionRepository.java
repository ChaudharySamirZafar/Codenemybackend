package codenemy.api.Submission.Repository;

import codenemy.api.Submission.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    @Query("select s.problem.id from Submission s where s.user.id = :userId")
    Set<Integer> findSubmissionByUser(@Param("userId") int userId);
    @Query("select s from Submission s where s.user.id = :userId and s.problem.id = :problemId order by s.points")
    List<Submission> findSubmissionByUserAndProblem(@Param("userId") int userId, @Param("problemId") int problemId);
}
