package codenemy.api.Compiler.Service;


import codenemy.api.Compiler.Service.LanguageCompilerService.JavaCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.LanguageCompilerServiceIF;
import codenemy.api.Compiler.Service.LanguageCompilerService.PythonCompilerService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

@Service
public class CompilerServiceFactory {

    public LanguageCompilerServiceIF getSpecificLanguageCompilerService(String language){

        switch (language) {
            case "java", "Java" -> {
                return new JavaCompilerService();
            }
            case "python", "Python", "python3", "Python3" -> {
                return new PythonCompilerService();
            }
            default -> throw new NotYetImplementedException(language + " does not have a compiler service yet.");
        }
    }
}
