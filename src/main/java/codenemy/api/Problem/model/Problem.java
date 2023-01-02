package codenemy.api.Problem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 30/12/2022
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
