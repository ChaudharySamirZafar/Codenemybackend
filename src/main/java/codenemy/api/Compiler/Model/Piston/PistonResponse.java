package codenemy.api.Compiler.Model.Piston;

/**
 * This record stores the content of the response from Piston API.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public record PistonResponse(String language, String version, Run run){
}

