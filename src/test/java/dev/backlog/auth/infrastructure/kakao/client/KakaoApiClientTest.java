package dev.backlog.auth.infrastructure.kakao.client;

import dev.backlog.auth.infrastructure.kakao.config.KakaoProperties;
import dev.backlog.auth.infrastructure.kakao.dto.KakaoMemberResponse;
import dev.backlog.auth.infrastructure.kakao.dto.KakaoTokens;
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
class KakaoApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KakaoProperties kakaoProperties;

    @InjectMocks
    private KakaoApiClient kakaoApiClient;

    @Test
    void fetchToken() {
        String authCode = "authCode";
        KakaoTokens kakaoTokens = new KakaoTokens(
                "accessToken",
                "tokenType",
                "refreshToken",
                1000,
                1000
        );

        when(kakaoProperties.getClientId()).thenReturn("testClientId");
        when(kakaoProperties.getRedirectUrl()).thenReturn("testRedirectUrl");
        when(kakaoProperties.getClientSecret()).thenReturn("testClientSecret");
        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(KakaoTokens.class)))
                .thenReturn(kakaoTokens);

        KakaoTokens expectedTokens = kakaoApiClient.fetchToken(authCode);

        assertAll(
                () -> assertThat(expectedTokens).isNotNull(),
                () -> assertThat(expectedTokens.accessToken()).isEqualTo(kakaoTokens.accessToken()),
                () -> assertThat(expectedTokens.refreshToken()).isEqualTo(kakaoTokens.refreshToken()),
                () -> assertThat(expectedTokens.tokenType()).isEqualTo(kakaoTokens.tokenType()),
                () -> assertThat(expectedTokens.expiresIn()).isEqualTo(kakaoTokens.expiresIn()),
                () -> assertThat(expectedTokens.refreshTokenExpiresIn()).isEqualTo(kakaoTokens.refreshTokenExpiresIn())
        );
    }

    @Test
    void fetchMember() {
        KakaoMemberResponse response = new KakaoMemberResponse(1L, null);
        ResponseEntity<KakaoMemberResponse> mockResponse = ResponseEntity.ok(response);

        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(KakaoMemberResponse.class)))
                .thenReturn(mockResponse);

        KakaoMemberResponse result = kakaoApiClient.fetchMember("accessToken");

        assertThat(result).isNotNull();
    }

}
