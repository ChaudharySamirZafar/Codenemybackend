package codenemy.api.Compiler.Model;

import java.util.List;

/**
 * This record a DTO. It is sent back to the client when they run the code with the output, result and errors of their code.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public record  SingleTestCaseResult(String input, String expectedOutput, String userOutput, List<String> stdOut, boolean pass, List<String> error){

}
