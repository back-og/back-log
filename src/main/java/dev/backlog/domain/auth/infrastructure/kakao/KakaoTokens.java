package dev.backlog.domain.auth.infrastructure.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokens {

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private int expiresIn;

    private int refreshTokenExpiresIn;
}
