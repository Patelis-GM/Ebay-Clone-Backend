package silkroad.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JWTUtilities {

    private static final String JWT_ISSUER = "SilkRoad";
    private static final String JWT_SECRET = "c0e6e95bf492c34cf00d26d689499975aa02426c7da37844502778b3cee6d709";
    public static final String JWT_RESPONSE_HEADER = "JWT";
    private static final long JWT_DURATION = 30000000L;
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private static final String JWT_ROLE_CLAIM_KEY = "Role";

    public static DecodedJWT verifyJWT(HttpServletRequest request) {

        try {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String JSONWebToken = authorizationHeader.substring(JWTUtilities.AUTHORIZATION_HEADER_PREFIX.length());
            Algorithm algorithm = Algorithm.HMAC256(JWTUtilities.JWT_SECRET.getBytes());
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            return jwtVerifier.verify(JSONWebToken);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> extractLoad(DecodedJWT decodedJWT) {
        List<String> load = new ArrayList<>();
        load.add(decodedJWT.getSubject());
        load.add(decodedJWT.getClaim(JWT_ROLE_CLAIM_KEY).toString().replace("\"",""));
        System.out.println(load.get(0));
        System.out.println(load.get(1));
        return load;
    }


    public static String generateJWT(Authentication authentication) {

        UserInformation user = (UserInformation) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());

        String JSONWebToken = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(JWT_ROLE_CLAIM_KEY, user.getAuthorities().iterator().next().getAuthority())
                .withExpiresAt(new Date(System.currentTimeMillis() + (JWT_DURATION * 1000)))
                .withIssuer(JWT_ISSUER)
                .sign(algorithm);

        return JSONWebToken;
    }

    public static boolean isBearerOfJWT(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorizationHeader != null && authorizationHeader.startsWith(JWTUtilities.AUTHORIZATION_HEADER_PREFIX);
    }


}
