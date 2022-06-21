package by.senla.training.bulyha.pricemonitoring.security;

import by.senla.training.bulyha.pricemonitoring.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationHours}")
    private long jwtExpirationHours;

    @Value("${jwt.expirationIfRememberedYears}")
    private long jwtExpirationIfRememberedYears;

    @Value("${jwt.authorization}")
    private String authorization;

    @Value("${jwt.tokenIdentifier}")
    private String tokenIdentifier;

    @Autowired
    private TokenService tokenService;


    public String generateJwt(Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authorization, authorities)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setIssuedAt(Date.valueOf(LocalDate.now()))
                .setExpiration(Date.from(Instant.from(ZonedDateTime.now().plusHours(jwtExpirationHours))))
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public java.util.Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final java.util.Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new java.util.Date());
    }

    UsernamePasswordAuthenticationToken getAuthentication(final String token, final UserDetails userDetails) {
        final Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(authorization).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

    public void existTokenByName(String token) {
        tokenService.existTokenByName(token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(authorization);
        if (bearer != null && bearer.startsWith(tokenIdentifier)) {
            return bearer.substring(tokenIdentifier.length());
        }
        return null;
    }

    public String getRoleByToken(HttpServletRequest httpServletRequest) {
        String token = resolveToken(httpServletRequest);
        final Claims claims = getAllClaimsFromToken(token);
        return Arrays.stream(claims.get(authorization).toString().split(","))
                .map(el -> el.replaceAll("[\\[\\]]", ""))
                .map(String::valueOf).collect(Collectors.joining(", "));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
}