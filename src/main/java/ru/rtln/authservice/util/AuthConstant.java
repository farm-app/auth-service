package ru.rtln.authservice.util;

/**
 * Auth provider constants.
 */
public class AuthConstant {

    public static final String BEARER = "Bearer ";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String SCOPE = "scope";
    public static final String GRANT_TYPE = "grant_type";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    private AuthConstant() {
        throw new AssertionError("No instances to use");
    }

    public static final String[] INTERNAL_ENDPOINTS = {
            "/internal/**",
            "/actuator/**"
    };
}
