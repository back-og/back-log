package dev.backlog.auth.infrastructure.kakao.authcode;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.infrastructure.kakao.config.KakaoProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoAuthCodeRequestUrlProviderTest {

    @Mock
    private KakaoProperties kakaoProperties;

    @InjectMocks
    private KakaoAuthCodeRequestUrlProvider provider;

    @Test
    void oAuthProviderTest() {
        OAuthProvider oAuthProvider = provider.oAuthProvider();

        assertThat(oAuthProvider).isEqualTo(OAuthProvider.KAKAO);
    }

    @Test
    void provideTest() {
        String resultUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=testClientId&redirect_uri=https://testClientId/redirect";

        when(kakaoProperties.getClientId()).thenReturn("testClientId");
        when(kakaoProperties.getRedirectUrl()).thenReturn("https://testClientId/redirect");

        String expectedUrl = provider.provide();

        assertThat(resultUrl).isEqualTo(expectedUrl);
    }

}
