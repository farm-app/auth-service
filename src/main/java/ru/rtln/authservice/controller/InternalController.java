package ru.rtln.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rtln.authservice.model.AuthRequest;
import ru.rtln.authservice.model.TokenInfoModel;
import ru.rtln.authservice.model.UserInfoModel;
import ru.rtln.authservice.service.impl.KeycloakAuthProvider;
import ru.rtln.authservice.util.AuthConstant;
import ru.rtln.authservice.util.CookieUtil;
import ru.rtln.common.model.SuccessModel;
import ru.rtln.common.security.keycloak.auth.TokenIntrospectModel;

import java.util.List;

@Slf4j
@RequestMapping("/internal")
@RestController
public class InternalController {

    private final KeycloakAuthProvider keycloakAuthProvider;
    private final AuthRequest authRequest;
    private final CookieUtil cookieUtil;

    public InternalController(KeycloakAuthProvider keycloakAuthProvider,
                              @Qualifier("ServiceAccountAuthRequest") AuthRequest authRequest,
                              CookieUtil cookieUtil) {
        this.keycloakAuthProvider = keycloakAuthProvider;
        this.authRequest = authRequest;
        this.cookieUtil = cookieUtil;
    }

    /**
     * Validate and renew tokens if required.
     *
     * @param accessToken  accessToken token
     * @param refreshToken refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<SuccessModel<TokenIntrospectModel>> refreshIfRequire(@CookieValue(value = AuthConstant.ACCESS_TOKEN, required = false) String accessToken,
                                                                               @CookieValue(value = AuthConstant.REFRESH_TOKEN, required = false) String refreshToken,
                                                                               HttpServletResponse response) {
        TokenIntrospectModel tokenIntrospectModel = keycloakAuthProvider.introspectToken(accessToken);
        if (tokenIntrospectModel.isActive()) {
            log.debug("Access token valid");
        } else {
            log.debug("Access token is invalid. Generate new tokens");
            TokenInfoModel tokenInfo = keycloakAuthProvider.tokenAuthentication(refreshToken);
            tokenIntrospectModel = keycloakAuthProvider.introspectToken(tokenInfo.getAccessToken());
            cookieUtil.addAuthCookieToResponse(tokenInfo, response);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessModel<>(HttpStatus.OK.value(), "Refresh tokens", tokenIntrospectModel));
    }

    @GetMapping("/users")
    public ResponseEntity<SuccessModel<List<UserInfoModel>>> getAllUsers() {
        TokenInfoModel tokenInfoModel = keycloakAuthProvider.passwordAuthentication(authRequest);
        List<UserInfoModel> users = keycloakAuthProvider.getUsers(tokenInfoModel.getAccessToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessModel<>(HttpStatus.OK.value(), "Get all users", users));
    }
}
