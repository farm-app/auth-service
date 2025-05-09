package ru.rtln.authservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Provider auth model.
 */
@Data
public class TokenInfoModel {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer accessExpiresIn;

    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;
}
