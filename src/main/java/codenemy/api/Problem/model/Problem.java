package codenemy.api.Problem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * This class is mapped to the `Problem` table in the database.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tag> tags;
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TestCase> testCases;
    @OneToMany(mappedBy = "problem", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProblemLanguage> ProblemLanguages;
    private String difficulty;
    @Transient
    private boolean solved;
}
