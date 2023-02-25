package codenemy.api.Problem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * This class is mapped to the `ProblemLanguage` table in the database.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    @Column(insertable = false, updatable = false)
    @JsonIgnore
    private int problem_id;
    @Column(insertable = false, updatable = false)
    @JsonIgnore
    private int language_id;
    private String code;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    @JsonIgnore
    private Problem problem;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
    private String testRunOne;
    private String testRunAll;
}
