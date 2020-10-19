package com.example.taskapp.authmanagement.utils;

import com.example.taskapp.authmanagement.config.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtUtils {

    public static String createToken(Map<String, Object> claims, String subject, Reader reader) throws IOException {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpirationDate(SecurityConstants.EXPIRATION_HOURS))
                .signWith(SignatureAlgorithm.RS256, PemUtils.readPrivateKeyFromReader(reader, SecurityConstants.RSA_ALGORITHM)).compact();
    }

    private static Date getExpirationDate(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Map<String, Object> generateClaims(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.ROLE, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return claims;
    }

    public static Resource loadKey(){
        return new ClassPathResource(SecurityConstants.TOKEN_KEY_LOCATION);
    }
}
