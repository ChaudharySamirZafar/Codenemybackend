package codenemy.api.Compiler.Model;

/**
 * @author samir.zafar
 * @version 1.0
 * @since 12/09/22
 */
public record Request(String language, String script, int problemId, String username, int userId) {
}