package dev.backlog.auth.domain.oauth.authcode;

import dev.backlog.auth.domain.oauth.OAuthProvider;

public interface AuthCodeRequestUrlProvider {

    OAuthProvider oAuthProvider();

    String provide();

}
