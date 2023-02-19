package codenemy.api.Compiler.Model.Piston;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<PistonFile> getFiles() {
        return files;
    }


    public String getStdin() {
        return stdin;
    }

    public String getArgs() {
        return args;
    }
}
