package dev.backlog.auth.domain.oauth.dto;

import dev.backlog.auth.domain.oauth.OAuthProvider;

public record SignupRequest(
        String blogTitle,
        String introduction,
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
