package ru.rtln.authservice.util;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.rtln.authservice.model.TokenInfoModel;

@Component
public class CookieUtil {

    private final String cookieSameSite;
    private final Boolean cookieSecure;

    public CookieUtil(@Value("${settings.cookie.same-site}") String cookieSameSite,
                      @Value("${settings.cookie.secure}") Boolean cookieSecure) {
        this.cookieSameSite = cookieSameSite;
        this.cookieSecure = cookieSecure;
    }

    public void addAuthCookieToResponse(TokenInfoModel authInfo, HttpServletResponse response) {
        if (authInfo == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        var accessToken = buildCookie(AuthConstant.ACCESS_TOKEN, authInfo.getAccessToken(),
                authInfo.getAccessExpiresIn());
        var refreshToken = buildCookie(AuthConstant.REFRESH_TOKEN, authInfo.getRefreshToken(),
                authInfo.getRefreshExpiresIn());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }

    public void resetAuthCookie(HttpServletResponse response) {
        var accessToken = buildCookie(AuthConstant.ACCESS_TOKEN, null, 0);
        var refreshToken = buildCookie(AuthConstant.REFRESH_TOKEN, null, 0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }

    private Cookie buildCookie(String name, @Nullable String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", cookieSameSite);
        return cookie;
    }
}
