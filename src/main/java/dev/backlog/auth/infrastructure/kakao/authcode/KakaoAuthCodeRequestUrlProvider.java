package dev.backlog.auth.infrastructure.kakao.authcode;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.authcode.AuthCodeRequestUrlProvider;
import dev.backlog.auth.infrastructure.kakao.config.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private static final String AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String RESPONSE_TYPE = "code";

    private final KakaoProperties kakaoProperties;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString(AUTHORIZE_URL)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUrl())
                .toUriString();
    }

}
