package dev.backlog.auth.domain.oauth.dto;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.user.domain.Email;

public record OAuthInfoResponse(
        String nickname,
        String profileImage,
        Email email,
        OAuthProvider oAuthProvider,
        String oAuthProviderId
) {

    public static OAuthInfoResponse of(
            String nickname,
            String profileImage,
            Email email,
            OAuthProvider oAuthProvider,
            String oAuthProviderId
    ) {
        return new OAuthInfoResponse(
                nickname,
                profileImage,
                email,
                oAuthProvider,
                oAuthProviderId
        );
    }

}
