package dev.backlog.domain.auth.model.oauth;

import dev.backlog.domain.auth.model.oauth.dto.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.dto.OAuthLoginAndSignUpParams;
import dev.backlog.domain.user.model.OAuthProvider;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();

    String requestAccessToken(OAuthLoginAndSignUpParams params);

    OAuthInfoResponse requestOauthInfo(String accessToken);

}
