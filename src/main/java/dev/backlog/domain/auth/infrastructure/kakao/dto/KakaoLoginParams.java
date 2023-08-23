package dev.backlog.domain.auth.infrastructure.kakao.dto;

import dev.backlog.domain.user.model.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginParams {

    private String authorizationCode;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public OAuthProvider getOauthProvider() {
        return OAuthProvider.KAKAO;
    }

}
