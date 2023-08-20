package dev.backlog.domain.auth.model.oauth;

import dev.backlog.domain.user.model.OAuthProvider;
import lombok.Builder;

@Builder
public record OAuthInfoResponse(
        String nickname,
        String profileImage,
        String email,
        Long oauthProviderId,
        OAuthProvider oauthProvider) {
}
