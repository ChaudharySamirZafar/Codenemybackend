package codenemy.api.Problem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
public class TagTest {
    Tag sut;

    @Test
    void tagGettersAndSetters(){
        sut = new Tag();

        String newTag = "newTag";
        int newId = 15;

        sut.setTag(newTag);
        sut.setId(newId);


        assertEquals(newTag, sut.getTag());
        assertEquals(newId, sut.getId());
    }
}
