package codenemy.api.Compiler.Service;


import codenemy.api.Compiler.Service.LanguageCompilerService.JavaCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.JavaScriptCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.LanguageCompilerServiceIF;
import codenemy.api.Compiler.Service.LanguageCompilerService.PythonCompilerService;
import codenemy.api.Util.CompilerUtility;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 01/01/2023
 */
@Service
public class CompilerServiceFactory {

    public LanguageCompilerServiceIF getSpecificLanguageCompilerService(String language){

        switch (language) {
            case "java", "Java" -> {
                return new JavaCompilerService(new CompilerUtility());
            }
            case "python", "Python", "python3", "Python3" -> {
                return new PythonCompilerService(new CompilerUtility());
            }
            case "javascript", "js", "JavaScript" -> {
                return new JavaScriptCompilerService(new CompilerUtility());
            }
            default -> throw new NotYetImplementedException(language + " does not have a compiler service yet.");
        }
    }
}
