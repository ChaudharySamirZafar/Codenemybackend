package codenemy.api.Compiler.Service;

import codenemy.api.Compiler.Service.LanguageCompilerService.JavaCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.JavaScriptCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.LanguageCompilerServiceIF;
import codenemy.api.Compiler.Service.LanguageCompilerService.PythonCompilerService;
import codenemy.api.Util.CompilerUtility;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;

/**
 * This class is a factory class that is used to create and return specific language
 * compiler services based on the language input provided to it.
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Component
public class CompilerServiceFactory {

    /**
     * This method takes a language string as input and returns the appropriate compiler service for that language.
     * It implements the Factory Pattern by creating and returning instances of the appropriate language-specific
     * compiler service classes.
     * @param language - the programming language the user is using
     * @return the specific language CompilerService class
     */
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
