package ru.rtln.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.rtln.common.security.secret.key.SecretKeyAuthenticationFilter;

import static ru.rtln.authservice.util.AuthConstant.INTERNAL_ENDPOINTS;

/**
 * Secure config.
 */
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           @Value("${settings.security.header-key-name}") String headerName,
                                           @Value("${settings.security.auth-service-key}") String secretKeyValue) throws Exception {
        http
                .securityMatcher(INTERNAL_ENDPOINTS)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value())))
                .addFilterBefore(new SecretKeyAuthenticationFilter(headerName, secretKeyValue), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
