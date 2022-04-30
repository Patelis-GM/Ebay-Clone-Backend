package silkroad.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import silkroad.dtos.user.request.UserCredentials;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            UserCredentials user = objectMapper.readValue(request.getInputStream(), UserCredentials.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        String JSONWebToken = JWTUtilities.generateJWT(authentication);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader(JWTUtilities.JWT_RESPONSE_HEADER, JSONWebToken);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {

        super.unsuccessfulAuthentication(request, response, authenticationException);

//        SilkRoadExceptionDTO silkRoadException;
//
//        if (authenticationException instanceof DisabledException) {
//            silkRoadException = new SilkRoadExceptionDTO(authenticationException.getClass().getName(), null, LoginException.LOGIN_USER_NOT_APPROVED);
//            PrintWriter printWriter = response.getWriter();
//            ObjectMapper objectMapper = new ObjectMapper();
//            printWriter.println(objectMapper.writeValueAsString(silkRoadException));
//            response.setContentType(APPLICATION_JSON_VALUE);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//
//        } else if (authenticationException instanceof BadCredentialsException) {
//            silkRoadException = new SilkRoadExceptionDTO(authenticationException.getClass().getName(), null, LoginException.LOGIN_USER_NOT_FOUND);
//            PrintWriter printWriter = response.getWriter();
//            ObjectMapper objectMapper = new ObjectMapper();
//            printWriter.println(objectMapper.writeValueAsString(silkRoadException));
//            response.setContentType(APPLICATION_JSON_VALUE);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        } else

    }
}
