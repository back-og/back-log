package dev.backlog.auth.infrastructure.github.authCode;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.infrastructure.github.config.GithubProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubAuthCodeRequestUrlProviderTest {

    @Mock
    private GithubProperties githubProperties;

    @InjectMocks
    private GithubAuthCodeRequestUrlProvider provider;

    @Test
    void oAuthProvider() {
        OAuthProvider oAuthProvider = provider.oAuthProvider();

        assertThat(oAuthProvider).isEqualTo(OAuthProvider.GITHUB);
    }

    @Test
    void provideTest() {
        String resultUrl = "https://github.com/login/oauth/authorize?client_id=testClientId&redirect_uri=https://testClientId/redirect";

        when(githubProperties.getClientId()).thenReturn("testClientId");
        when(githubProperties.getRedirectUrl()).thenReturn("https://testClientId/redirect");

        String expectedUrl = provider.provide();

        assertThat(resultUrl).isEqualTo(expectedUrl);
    }

}
