package codenemy.api.Auth.filter;

import codenemy.api.Util.Utility;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;

/**
 * @author chaudhary samir zafar
 * @version 1.0
 * @since 28/12/2022
 */
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    /**
     * When a request is sent to the API this is the first bit of code that runs.
     * If the user is trying to log in or get a refresh token then the request will be filtered.
     * Else the auth token will be broken down & checked if it is correct.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/user/login") || request.getServletPath().equals("/api/user/refreshToken")) {
            filterChain.doFilter(request, response);
        }
        else {

            DecodedJWT decodedJWT = Utility.decodeJWT(request, response);

            if (decodedJWT != null) {
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }
            else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
