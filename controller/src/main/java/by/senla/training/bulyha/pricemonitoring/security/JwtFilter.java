package by.senla.training.bulyha.pricemonitoring.security;

import by.senla.training.bulyha.pricemonitoring.exception.AuthException;
import by.senla.training.bulyha.pricemonitoring.service.impl.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtFilter(JwtProvider jwtProvider, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = jwtProvider.resolveToken(request);
        String username;
        if (token != null) {
            try {
                jwtProvider.existTokenByName(token);
                username = jwtProvider.getUsernameFromToken(token);
            } catch (AuthException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error("Access closed. You are logged out and token is not valid.");
                return;
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error("Error getting username from token.", e);
                return;
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.warn("Time of token expired.", e);
                return;
            } catch (SignatureException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error("Username or password is incorrect.");
                return;
            } catch (MalformedJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error("Incorrect token.");
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                if (jwtProvider.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = jwtProvider
                            .getAuthentication(token, userDetails);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("Authorized user " + username + ", add to security context.");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}