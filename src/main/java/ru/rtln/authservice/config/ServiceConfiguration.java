package ru.rtln.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.rtln.authservice.model.AuthRequest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class ServiceConfiguration {

    @Bean("KeycloakRestTemplate")
    public RestTemplate restTemplate(@Value("${settings.keycloak.uri}") String keycloakUri,
                                     RestTemplateBuilder builder) {
        return builder
            .rootUri(keycloakUri)
            .setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .build();
    }

    @Bean("ServiceAccountAuthRequest")
    public AuthRequest providerServiceAccount(@Value("${settings.keycloak.service-account.username}") String username,
                                              @Value("${settings.keycloak.service-account.password}") String password) {
        return new AuthRequest(username, password);
    }
}
