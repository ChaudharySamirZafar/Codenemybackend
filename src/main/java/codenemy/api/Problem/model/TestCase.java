package codenemy.api.Problem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 30/12/2022
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    private String input;
    private String output;
    @Column(insertable = false, updatable = false)
    @JsonIgnore
    private int problemId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problemId", nullable = false)
    @JsonIgnore
    private Problem problem;

    @Override
    public String toString() {
        return "TestCase{" +
                "id='" + id + '\'' +
                ", input='" + input + '\'' +
                ", output='" + output + '\'';
    }
}
