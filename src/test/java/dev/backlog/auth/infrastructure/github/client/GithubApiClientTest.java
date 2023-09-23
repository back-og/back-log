package dev.backlog.auth.infrastructure.github.client;

import dev.backlog.auth.infrastructure.github.config.GithubProperties;
import dev.backlog.auth.infrastructure.github.dto.GithubMemberResponse;
import dev.backlog.auth.infrastructure.github.dto.GithubTokens;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GithubProperties githubProperties;

    @InjectMocks
    private GithubApiClient githubApiClient;

    @Test
    void fetchToken() {
        String authCode = "authCode";
        GithubTokens githubTokens = new GithubTokens(
                "scope",
                "accessToken",
                "tokenType",
                "refreshToken",
                1000,
                1000
        );

        when(githubProperties.getClientId()).thenReturn("testClientId");
        when(githubProperties.getRedirectUrl()).thenReturn("testRedirectUrl");
        when(githubProperties.getClientSecret()).thenReturn("testClientSecret");
        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(GithubTokens.class)))
                .thenReturn(githubTokens);

        GithubTokens expectedTokens = githubApiClient.fetchToken(authCode);

        assertAll(
                () -> assertThat(expectedTokens).isNotNull(),
                () -> assertThat(expectedTokens.scope()).isEqualTo(githubTokens.scope()),
                () -> assertThat(expectedTokens.accessToken()).isEqualTo(githubTokens.accessToken()),
                () -> assertThat(expectedTokens.refreshToken()).isEqualTo(githubTokens.refreshToken()),
                () -> assertThat(expectedTokens.tokenType()).isEqualTo(githubTokens.tokenType()),
                () -> assertThat(expectedTokens.expiresIn()).isEqualTo(githubTokens.expiresIn()),
                () -> assertThat(expectedTokens.refreshTokenExpiresIn()).isEqualTo(githubTokens.refreshTokenExpiresIn())
        );
    }

    @Test
    void fetchMember() {
        GithubMemberResponse response = new GithubMemberResponse(
                1L,
                "test123@gmail.com",
                "nickname",
                "imageUrl");
        ResponseEntity<GithubMemberResponse> mockResponse = ResponseEntity.ok(response);

        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(GithubMemberResponse.class)))
                .thenReturn(mockResponse);

        GithubMemberResponse result = githubApiClient.fetchMember("accessToken");

        assertThat(result).isNotNull();
    }

}
