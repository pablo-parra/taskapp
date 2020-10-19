package com.example.taskapp.authmanagement.config.constants;

/**
 * Security Constants class
 */
public class SecurityConstants {

    /**
     * Token Header
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token Prefix
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token Issuer
     */
    public static final String TOKEN_ISSUER = "TaskApp";

    /**
     * Expiration Hours
     */
    public static final int EXPIRATION_HOURS = 1;

    /**
     * RSA Algorithm
     */
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * Role
     */
    public static final String ROLE = "role";

    /**
     * Token Key Location
     */
    public static final String TOKEN_KEY_LOCATION = "keys/dev/taskapp.pem";

    /**
     * Auth Login Url
     */
    public static final String AUTH_LOGIN_URL = "/api/authenticate";

    /**
     * The constructor
     */
    private SecurityConstants(){ }
}
