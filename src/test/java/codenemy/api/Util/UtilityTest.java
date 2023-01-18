package codenemy.api.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 18/01/2023
 */
@ExtendWith(MockitoExtension.class)
public class UtilityTest {
    MockHttpServletResponse mockResponse;
    MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockResponse = new MockHttpServletResponse();
        mockRequest = new MockHttpServletRequest();
    }

    @Test
    void decodeJwtWithNoErrors() throws IOException {
        // Given
        String subject = "samir";
        String issuer = "/mockUrl";

        String authToken = JWT.create()
                .withSubject("samir")
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer("/mockUrl")
                .withClaim("roles", "null")
                .sign(Utility.algorithm);

        mockRequest.addHeader(AUTHORIZATION, "Bearer " + authToken);

        // When
        DecodedJWT decodedJWT =
                Utility.decodeJWT(mockRequest, mockResponse);

        // Then
        assertNotNull(decodedJWT);
        assertEquals(subject, decodedJWT.getSubject());
        assertEquals(issuer, decodedJWT.getIssuer());
        assertEquals("null", decodedJWT.getClaim("roles").asString());
    }

    // Test comment
    @Test
    void decodeJwtWithErrors() throws IOException {
        // Given
        mockRequest.addHeader(AUTHORIZATION, "Bearer testing");;

        // When
        Utility.decodeJWT(mockRequest, mockResponse);

        // Then
        assertEquals(403, mockResponse.getStatus());
        assertEquals("The token was expected to have 3 parts, but got 1.", mockResponse.getHeader("error"));
    }

    @Test
    void writeTokenValues() throws UnsupportedEncodingException, JSONException {
        // Given
        String mockAccessToken = "mockAccessToken";
        String mockRefreshToken = "mockRefreshToken";

        // When
        Utility.writeTokenValues(mockAccessToken, mockRefreshToken, mockResponse);
        JSONObject jsonObject = new JSONObject(mockResponse.getContentAsString());

        // Then
        assertEquals(APPLICATION_JSON_VALUE, mockResponse.getContentType());
        assertEquals(mockAccessToken, jsonObject.get("access_token"));
        assertEquals(mockRefreshToken, jsonObject.get("refresh_token"));
    }

    @Test
    void writeTokenValuesWithError() throws UnsupportedEncodingException, JSONException {
        // Given
        String mockAccessToken = "mockAccessToken";
        String mockRefreshToken = "mockRefreshToken";
        mockResponse.setOutputStreamAccessAllowed(false);

        // When
        Utility.writeTokenValues(mockAccessToken, mockRefreshToken, mockResponse);
    }

}
