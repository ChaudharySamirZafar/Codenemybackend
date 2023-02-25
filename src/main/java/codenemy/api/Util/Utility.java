package codenemy.api.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class Utility {
    public final static Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    /**
     * Reusable method to decode JSON Web Token
     */
    public static DecodedJWT decodeJWT(HttpServletRequest request, HttpServletResponse response) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        DecodedJWT decodedJWT = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());

                JWTVerifier verifier = JWT.require(algorithm).build();
                decodedJWT = verifier.verify(token);
            } catch (JWTVerificationException exception) {
                log.error("JWTVerificationException Error in decodeJWT - {}", exception.getMessage());

                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);

                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                } catch (IOException ioException) {
                    log.error("IOException Error - {}", exception.getMessage());
                }
            }
        }

        return decodedJWT;
    }

    /**
     * Reusable method to write access & refresh tokens to a response.
     */
    public static void writeTokenValues(String access_token, String refresh_token, HttpServletResponse response) {

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        response.setContentType(APPLICATION_JSON_VALUE);

        try {
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (Exception exception) {
            log.error("{} Error in writeTokenValues {}", exception.getClass(), exception.getMessage());
        }
    }
}
