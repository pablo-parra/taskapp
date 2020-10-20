package com.example.taskapp.authmanagement.config.filters;

import com.example.taskapp.authmanagement.config.authentication.Credentials;
import com.example.taskapp.authmanagement.config.constants.SecurityConstants;
import com.example.taskapp.authmanagement.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class JwtFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;


    public JwtFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var credentials = new Credentials(request.getHeader(HttpHeaders.AUTHORIZATION));
        var authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getName(), credentials.getPassword(), new ArrayList<>());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        var user = ((User) authentication.getPrincipal());
        var token = generateToken(user);
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }

    public String generateToken(User user) {
        try (Reader reader = new InputStreamReader(JwtUtils.loadKey().getInputStream())) {
            Map<String, Object> claims = JwtUtils.generateClaims(user);
            return JwtUtils.createToken(claims, user.getUsername(), reader);
        } catch (Exception ex) {
            log.error("Something went wrong while creating the JWT token: {}", ex.getMessage());
            throw new BadCredentialsException("Token not valid");
        }
    }
}
