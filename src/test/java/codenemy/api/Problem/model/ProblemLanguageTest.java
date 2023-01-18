package codenemy.api.Problem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
public class ProblemLanguageTest {
    ProblemLanguage sut;

    @Test
    void problemLanguageGettersAndSetters(){
        sut = new ProblemLanguage();

        Problem problem = new Problem();
        int newLangId = 1;
        Language language = new Language();
        int newProblemId = 5;
        int newId = 5;
        String newCode = "newCode";
        String newTestRunOne = "newTestRunOne";
        String newTestRunAll = "newTestRunAll";

        sut.setProblem(problem);
        sut.setLanguage_id(newLangId);
        sut.setLanguage(language);
        sut.setProblem_id(newProblemId);
        sut.setId(newId);
        sut.setCode(newCode);
        sut.setTestRunAll(newTestRunAll);
        sut.setTestRunOne(newTestRunOne);

        assertEquals(problem, sut.getProblem());
        assertEquals(newLangId, sut.getLanguage_id());
        assertEquals(language, sut.getLanguage());
        assertEquals(newProblemId, sut.getProblem_id());
        assertEquals(newId, sut.getId());
        assertEquals(newCode, sut.getCode());
        assertEquals(newTestRunOne, sut.getTestRunOne());
        assertEquals(newTestRunAll, sut.getTestRunAll());
    }
}
