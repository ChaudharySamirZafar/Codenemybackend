package codenemy.api.Submission.Repository;

import codenemy.api.Submission.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    @Query("select s.problem.id from Submission s where s.user.id = :userId")
    Set<Integer> findSubmissionByUserAndProblem(@Param("userId") int userId);
    @Query("select s from Submission s where s.user.id = :userId and s.problem.id = :problemId order by s.points")
    List<Submission> findSubmissionByUserAndProblem(@Param("userId") int userId, @Param("problemId") int problemId);
}
