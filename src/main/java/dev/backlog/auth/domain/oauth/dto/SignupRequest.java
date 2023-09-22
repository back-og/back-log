package dev.backlog.auth.domain.oauth.dto;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank
        String blogTitle,
        @NotBlank
        String introduction,
        @NotBlank
        String authCode,
        OAuthProvider oAuthProvider
) {

    public static SignupRequest of(
            String blogTitle,
            String introduction,
            String authCode,
            OAuthProvider oAuthProvider
    ) {
        return new SignupRequest(
                blogTitle,
                introduction,
                authCode,
                oAuthProvider
        );
    }

}
