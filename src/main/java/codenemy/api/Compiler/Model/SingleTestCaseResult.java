package codenemy.api.Compiler.Model;

import java.util.List;

/**
 * @author samir.zafar
 * @version 1.0
 * @since 12/09/22
 */
public record  SingleTestCaseResult(String input, String expectedOutput, String userOutput, List<String> stdOut, boolean pass){

}
