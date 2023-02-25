package codenemy.api.Compiler.Model.Piston;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This class is used to build an object that contains all the relevant information for a valid Piston API Request.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class PistonRequest {
    private final String language;
    private String version;
    private final List<PistonFile> files;
    private final String stdin;

    private String args;

    public PistonRequest(String language, List<PistonFile> files) {
        this.language = language.toLowerCase();
        this.files = files;
        this.stdin = "";
        this.args = "";
    }
}
