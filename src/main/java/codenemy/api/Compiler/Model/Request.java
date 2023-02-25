package codenemy.api.Compiler.Model;

/**
 * This record is a DTO, used for when users want to compile code.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public record Request(String language, String script, int problemId, String username, int userId) {
}