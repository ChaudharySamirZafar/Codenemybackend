package codenemy.api.Compiler.Model;

import java.util.List;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
public record  SingleTestCaseResult(String input, String expectedOutput, String userOutput, List<String> stdOut, boolean pass){

}
