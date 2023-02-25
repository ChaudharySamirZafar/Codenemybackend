package codenemy.api.Compiler.Service;

import codenemy.api.Compiler.Service.LanguageCompilerService.JavaCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.JavaScriptCompilerService;
import codenemy.api.Compiler.Service.LanguageCompilerService.LanguageCompilerServiceIF;
import codenemy.api.Compiler.Service.LanguageCompilerService.PythonCompilerService;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CompilerServiceFactoryTest {

    private CompilerServiceFactory sut;

    @BeforeEach
    void setUp(){

        sut = new CompilerServiceFactory();
    }

    @Test
    void loadJavaCompilerService(){

        // When
        LanguageCompilerServiceIF result = sut.getSpecificLanguageCompilerService("Java");
        LanguageCompilerServiceIF resultTwo = sut.getSpecificLanguageCompilerService("java");

        // Then
        assertThat(result, instanceOf(JavaCompilerService.class));
        assertThat(resultTwo, instanceOf(JavaCompilerService.class));
    }

    @Test
    void loadPythonCompilerService(){

        // When
        LanguageCompilerServiceIF result = sut.getSpecificLanguageCompilerService("python");
        LanguageCompilerServiceIF resultTwo = sut.getSpecificLanguageCompilerService("Python");
        LanguageCompilerServiceIF resultThree = sut.getSpecificLanguageCompilerService("Python3");
        LanguageCompilerServiceIF resultFour = sut.getSpecificLanguageCompilerService("python3");

        // Then
        assertThat(result, instanceOf(PythonCompilerService.class));
        assertThat(resultTwo, instanceOf(PythonCompilerService.class));
        assertThat(resultThree, instanceOf(PythonCompilerService.class));
        assertThat(resultFour, instanceOf(PythonCompilerService.class));
    }

    @Test
    void loadJavaScriptCompilerService(){

        // When
        LanguageCompilerServiceIF result = sut.getSpecificLanguageCompilerService("javascript");
        LanguageCompilerServiceIF resultTwo = sut.getSpecificLanguageCompilerService("js");
        LanguageCompilerServiceIF resultThree = sut.getSpecificLanguageCompilerService("JavaScript");

        // Then
        assertThat(result, instanceOf(JavaScriptCompilerService.class));
        assertThat(resultTwo, instanceOf(JavaScriptCompilerService.class));
        assertThat(resultThree, instanceOf(JavaScriptCompilerService.class));
    }

    @Test
    void loadCompilerServiceThatDoesntExist(){

        assertThatThrownBy(() -> sut.getSpecificLanguageCompilerService("C#"))
                .hasMessage("C# does not have a compiler service yet.")
                .isInstanceOf(NotYetImplementedException.class);
    }
}
