package silkroad.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import silkroad.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isBearerOfJWT = JWTUtilities.isBearerOfJWT(request);

        if (request.getServletPath().equals("/login")) {

            if (!isBearerOfJWT)
                filterChain.doFilter(request, response);

            else {

                DecodedJWT decodedJWT = JWTUtilities.verifyJWT(request);

                if (decodedJWT == null)
                    filterChain.doFilter(request, response);

                else
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
            }

        } else {

            if (isBearerOfJWT) {

                DecodedJWT decodedJSONWebToken = JWTUtilities.verifyJWT(request);

                if (decodedJSONWebToken != null) {

                    String username = decodedJSONWebToken.getSubject();

                    UserInformation userDetails = (UserInformation) this.userService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

                filterChain.doFilter(request, response);

            } else
                filterChain.doFilter(request, response);
        }

    }

}
