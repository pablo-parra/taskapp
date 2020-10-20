package com.example.taskapp.authmanagement.config.authentication;

import com.example.taskapp.authmanagement.config.constants.SecurityConstants;
import com.example.taskapp.authmanagement.utils.PemUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Jwt Authentication
 */
@Slf4j
public class JwtAuthentication extends BasicAuthenticationFilter {

    public JwtAuthentication(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        var authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try(Reader reader = new InputStreamReader(loadKey().getInputStream())) {

                var parsedToken = Jwts.parser()
                        .setSigningKey(PemUtils.readPrivateKeyFromReader(reader, SecurityConstants.RSA_ALGORITHM))
                        .parseClaimsJws(token.replace("Bearer ", ""));

                var username = parsedToken
                        .getBody()
                        .getSubject();

                var authorities = ((List<?>) parsedToken.getBody()
                        .get(SecurityConstants.ROLE)).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

                if (StringUtils.isNotEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException exception) {
                log.warn("Request to parse JWT : {} failed : {}", token, exception.getMessage());
            } catch (IOException ioException) {
                log.error("Something went wrong while creating the JWT token: {}", ioException.getMessage());
                throw new BadCredentialsException("Token not valid");
            }
        }

        return null;
    }

    private Resource loadKey(){
        return new ClassPathResource(SecurityConstants.TOKEN_KEY_LOCATION);
    }
}
