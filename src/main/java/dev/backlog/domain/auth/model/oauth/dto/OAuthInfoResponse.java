package dev.backlog.domain.auth.model.oauth.dto;

import dev.backlog.domain.user.model.Email;
import dev.backlog.domain.user.model.OAuthProvider;
import lombok.Builder;

@Builder
public record OAuthInfoResponse(
        String nickname,
        String profileImage,
        Email email,
        Long oauthProviderId,
        OAuthProvider oauthProvider
) {
}
