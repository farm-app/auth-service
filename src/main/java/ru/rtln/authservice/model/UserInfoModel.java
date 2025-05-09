package ru.rtln.authservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * User info model.
 */
@JsonPropertyOrder({
        "email",
        "username",
        "enabled",
        "admin",
        "firstName",
        "lastName",
        "patronymic"
})
@Data
public class UserInfoModel {

    private String email;
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;

    @Getter(AccessLevel.NONE)
    private UserAttributes attributes;

    /**
     * Compute user's patronymic (if it exists), based on the value of the full name.
     * Example fullName value (ФИО): "Петров Василий Романович"
     *
     * @return user's patronymic
     */
    @JsonProperty("patronymic")
    public String getPatronymic() {
        if (attributes == null || attributes.fullName == null || attributes.fullName.isEmpty()) {
            return null;
        }
        String[] fullNameValues = attributes.fullName.get(0).split(" ");
        return fullNameValues.length == 3 ? fullNameValues[2].trim() : null;
    }

    /**
     * Compute user role
     *
     * @return true - user is admin
     */
    @JsonProperty("admin")
    public boolean isAdmin() {
        if (attributes == null || attributes.appAdmin == null) {
            return false;
        }
        return attributes.appAdmin.stream().findFirst().orElse(false);
    }

    /**
     * Compute user city
     *
     * @return city
     */
    @JsonProperty("city")
    public String city() {
        if (attributes == null || attributes.city == null) {
            return null;
        }
        return attributes.city.stream().findFirst().orElse(null);
    }

    /**
     * Compute userId
     *
     * @return userId
     */
    @JsonProperty("userId")
    public String userId() {
        if (attributes == null || attributes.userId == null) {
            return null;
        }
        return attributes.userId.stream().findFirst().orElse(null);
    }

    /**
     * Compute user permissions
     *
     * @return city
     */
    @JsonProperty("permissions")
    public List<String> permissions() {
        if (attributes == null) {
            return null;
        }
        return attributes.permissions;
    }

    /**
     * Represents user additional attributes from keycloak
     */
    @Data
    public static class UserAttributes {

        @JsonProperty(value = "appAdmin")
        private List<Boolean> appAdmin;

        @JsonProperty(value = "fullName")
        private List<String> fullName;

        @JsonProperty(value = "userId")
        private List<String> userId;

        @JsonProperty(value = "city")
        private List<String> city;

        @JsonProperty(value = "permissions")
        private List<String> permissions;
    }
}
