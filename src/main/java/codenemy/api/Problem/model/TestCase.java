package codenemy.api.Problem.model;

import codenemy.api.Problem.model.Problem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
