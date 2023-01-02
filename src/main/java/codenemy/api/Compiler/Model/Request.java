package codenemy.api.Compiler.Model;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
public record Request(String language, String script, int problemId, String username, int userId) {
}