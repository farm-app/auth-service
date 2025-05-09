package ru.rtln.authservice.service;

import ru.rtln.authservice.model.AuthRequest;
import ru.rtln.authservice.model.TokenInfoModel;
import ru.rtln.authservice.model.UserInfoModel;
import ru.rtln.common.security.keycloak.auth.TokenIntrospectModel;

import java.util.List;

/**
 * Auth provider operations.
 */
public interface AuthProvider {

    /**
     * Generate tokens by user credentials.
     */
    TokenInfoModel passwordAuthentication(AuthRequest authRequest);

    /**
     * Generate tokens by refresh token.
     */
    TokenInfoModel tokenAuthentication(String refreshToken);

    /**
     * Revoke refresh token.
     */
    void logout(String refreshToken);

    /**
     * Introspect token (access or refresh).
     */
    TokenIntrospectModel introspectToken(String token);

    /**
     * Get all users by access token.
     */
    List<UserInfoModel> getUsers(String accessToken);
}
