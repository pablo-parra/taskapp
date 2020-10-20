package com.example.taskapp.authmanagement.config.authentication;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Arrays;
import java.util.Base64;

/**
 * Credentials
 */
@Getter
public class Credentials {

    private final String name;

    private final String password;

    public Credentials(String basicAuth){
        String[] credentials = new String(Base64.getDecoder().decode(basicAuth.replace("Basic ", ""))).split(":");
        if(Arrays.stream(credentials).count() > 2) throw new BadCredentialsException("Bad Credentials");
        this.name = credentials[0];
        this.password = credentials[1];
    }
}
