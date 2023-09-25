package dev.backlog.auth.infrastructure.github.client;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.client.OAuthMemberClient;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.auth.infrastructure.github.dto.GithubMemberResponse;
import dev.backlog.auth.infrastructure.github.dto.GithubTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubMemberClient implements OAuthMemberClient {

    private final GithubApiClient githubApiClient;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GITHUB;
    }

    @Override
    public OAuthInfoResponse fetch(String authCode) {
        GithubTokens tokenInfo = githubApiClient.fetchToken(authCode);
        GithubMemberResponse response = githubApiClient.fetchMember(tokenInfo.accessToken());

        return response.toOAuthInfoResponse();
    }

}
