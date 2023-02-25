package codenemy.api.Compiler.Model.Piston;

/**
 * This record stores the content of the response from Piston API, specifically the Run JSON object.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
public record  Run(String stdout, String stderr, String code, String signal, String output){}

