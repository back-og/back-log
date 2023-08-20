package dev.backlog.domain.auth.infrastructure.kakao;

import dev.backlog.domain.user.model.OAuthProvider;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KakaoLoginParams {

    private String authorizationCode;
    private String blogTitle;

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public OAuthProvider getOauthProvider() {
        return OAuthProvider.KAKAO;
    }
}
