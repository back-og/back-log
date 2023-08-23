package dev.backlog.domain.auth.infrastructure.kakao.dto;

import dev.backlog.domain.user.model.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoSignUpParams {
    private String authorizationCode;
    private String blogTitle;
    private String introduction;

    public OAuthProvider getOauthProvider() {
        return OAuthProvider.KAKAO;
    }

}
