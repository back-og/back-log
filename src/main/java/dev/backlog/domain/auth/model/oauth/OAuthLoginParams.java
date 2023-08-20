package dev.backlog.domain.auth.model.oauth;

import dev.backlog.domain.user.model.OAuthProvider;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record OAuthLoginParams(
        String authorizationCode,
        String blogTitle,
        OAuthProvider oauthProvider) {

    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
