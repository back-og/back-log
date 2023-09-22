package dev.backlog.auth.infrastructure.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubTokens(
        String scope,
        String accessToken,
        String tokenType,
        String refreshToken,
        int expiresIn,
        int refreshTokenExpiresIn
) {
}
