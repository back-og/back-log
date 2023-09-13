package dev.backlog.auth.domain.oauth.client;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;

public interface OAuthMemberClient {

    OAuthProvider oAuthProvider();

    OAuthInfoResponse fetch(String authCode);

}
