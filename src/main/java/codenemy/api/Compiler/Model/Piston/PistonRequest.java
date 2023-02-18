package codenemy.api.Compiler.Model.Piston;

import codenemy.api.Compiler.Model.Piston.PistonFile;

import java.util.ArrayList;
import java.util.List;

public class PistonRequest {
    private String language;
    private String version;
    private List<PistonFile> files;
    private String stdin;

    private String args;

    public PistonRequest(String language, List<PistonFile> files) {
        this.language = language.toLowerCase();
        this.files = files;
        this.stdin = "";
        this.args = "";
        setVersion();
    }

    public void setVersion() {
        if (language.equalsIgnoreCase("java")) {
            version = "15.0.2";
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<PistonFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<PistonFile> files) {
        this.files = files;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
