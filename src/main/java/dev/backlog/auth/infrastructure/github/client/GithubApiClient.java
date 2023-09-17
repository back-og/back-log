package dev.backlog.auth.infrastructure.github.client;

import dev.backlog.auth.infrastructure.github.GithubTokens;
import dev.backlog.auth.infrastructure.github.config.GithubProperties;
import dev.backlog.auth.infrastructure.github.dto.GithubMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GithubApiClient {

    private static final String REQUEST_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String REQUEST_INFO_URL = "https://api.github.com/user";
    private static final String BEARER_TYPE = "Bearer ";

    private final RestTemplate restTemplate;
    private final GithubProperties githubProperties;

    public GithubTokens fetchToken(String authCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", githubProperties.getClientId());
        body.add("redirect_uri", githubProperties.getRedirectUrl());
        body.add("client_secret", githubProperties.getClientSecret());
        body.add("code", authCode);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        GithubTokens response = restTemplate.postForObject(REQUEST_TOKEN_URL, request, GithubTokens.class);

        assert response != null;
        return response;
    }

    public GithubMemberResponse fetchMember(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", BEARER_TYPE + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        GithubMemberResponse response = restTemplate.exchange(REQUEST_INFO_URL, HttpMethod.GET, request, GithubMemberResponse.class).getBody();

        assert response != null;
        return response;
    }

}
