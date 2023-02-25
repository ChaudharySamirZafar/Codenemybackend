package codenemy.api.Submission.Model;

import codenemy.api.Auth.model.User;
import codenemy.api.Problem.model.Problem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

/**
 * The `Submission` class is mapped to the `submission` table in the database.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(optional = false, fetch = LAZY)
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @OneToOne(optional = false, fetch = LAZY)
    @JsonIgnore
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;
    private LocalDateTime date;
    private String details;
    private int percentage;
    private int points;
}
