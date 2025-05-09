package ru.rtln.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rtln.authservice.model.AuthRequest;
import ru.rtln.authservice.model.TokenInfoModel;
import ru.rtln.authservice.service.impl.KeycloakAuthProvider;
import ru.rtln.authservice.util.AuthConstant;
import ru.rtln.authservice.util.CookieUtil;
import ru.rtln.common.model.SuccessModel;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RequestMapping
@RestController
public class AuthController {

    private final KeycloakAuthProvider keycloakAuthService;
    private final CookieUtil cookieUtil;
    private final List<String> usersWhitelist;

    public AuthController(
            KeycloakAuthProvider keycloakAuthService,
            CookieUtil cookieUtil,
            @Value("${settings.users-whitelist}") List<String> usersWhitelist
    ) {
        this.keycloakAuthService = keycloakAuthService;
        this.cookieUtil = cookieUtil;
        this.usersWhitelist = usersWhitelist;
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessModel<TokenInfoModel>> passwordAuthentication(
            @RequestBody AuthRequest authRequest,
            HttpServletResponse response
    ) {
        if (!usersWhitelist.isEmpty() && !usersWhitelist.contains(authRequest.getUsername())) {
            log.error("User {} not in whitelist: {}", authRequest.getUsername(), usersWhitelist);
            cookieUtil.resetAuthCookie(response);
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        TokenInfoModel tokenInfoModel = keycloakAuthService.passwordAuthentication(authRequest);
        cookieUtil.addAuthCookieToResponse(tokenInfoModel, response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessModel<>(HttpStatus.OK.value(), "Login", tokenInfoModel));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessModel<String>> logout(@CookieValue(AuthConstant.REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        keycloakAuthService.logout(refreshToken);
        cookieUtil.resetAuthCookie(response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessModel<>(HttpStatus.OK.value(), "Logout", "Success logout"));
    }
}
