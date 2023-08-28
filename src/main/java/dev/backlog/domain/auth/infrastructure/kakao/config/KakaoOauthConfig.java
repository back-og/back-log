package dev.backlog.domain.auth.infrastructure.kakao.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoOauthConfig {
    public static final String AUTHORIZE_URL = "/oauth/authorize";
    public static final String REQUEST_TOKEN_URL = "/oauth/token";
    public static final String REQUEST_INFO_URL = "/v2/user/me";

    private KakaoUrlProperties url;
    private String clientId;

}
