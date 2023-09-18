package dev.backlog.auth.infrastructure.github.authCode;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.authcode.AuthCodeRequestUrlProvider;
import dev.backlog.auth.infrastructure.github.config.GithubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GithubAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final String AUTHORIZE_URL = "https://github.com/login/oauth/authorize";

    private final GithubProperties githubProperties;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GITHUB;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString(AUTHORIZE_URL)
                .queryParam("client_id", githubProperties.getClientId())
                .queryParam("redirect_uri", githubProperties.getRedirectUrl())
                .toUriString();
    }

}
