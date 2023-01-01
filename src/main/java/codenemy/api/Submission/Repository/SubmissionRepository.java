package codenemy.api.Submission.Repository;

import codenemy.api.Submission.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    @Query("select s.problem.id from Submission s where s.user.id = :userId")
    Set<Integer> findSubmissionByUserAndProblem(@Param("userId") int userId);
}
