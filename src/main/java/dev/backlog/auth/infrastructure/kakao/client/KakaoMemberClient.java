package dev.backlog.auth.infrastructure.kakao.client;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.client.OAuthMemberClient;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.auth.infrastructure.kakao.dto.KakaoMemberResponse;
import dev.backlog.auth.infrastructure.kakao.dto.KakaoTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OAuthMemberClient {

    private final KakaoApiClient kakaoApiClient;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public OAuthInfoResponse fetch(String authCode) {
        KakaoTokens tokenInfo = kakaoApiClient.fetchToken(authCode);
        KakaoMemberResponse response = kakaoApiClient.fetchMember(tokenInfo.accessToken());

        return response.toOAuthInfoResponse();
    }

}
