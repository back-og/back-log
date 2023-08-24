package dev.backlog.domain.auth.model.oauth;

import dev.backlog.domain.auth.model.oauth.dto.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.dto.OAuthLoginAndSignUpRequest;
import dev.backlog.domain.user.model.OAuthProvider;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();

    String requestAccessToken(OAuthLoginAndSignUpRequest params);

    OAuthInfoResponse requestOauthInfo(String accessToken);

}
