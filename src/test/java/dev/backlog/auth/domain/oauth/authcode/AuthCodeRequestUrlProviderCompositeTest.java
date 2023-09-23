package dev.backlog.auth.domain.oauth.authcode;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.common.exception.InvalidAuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthCodeRequestUrlProviderCompositeTest {

    @Mock
    private AuthCodeRequestUrlProvider provider;

    @Mock
    private Set<AuthCodeRequestUrlProvider> providers;

    @InjectMocks
    private AuthCodeRequestUrlProviderComposite composite;

    @Test
    void provide() {
        OAuthProvider oAuthProvider = OAuthProvider.GITHUB;
        String resultUrl = "https://github.com/login/oauth/authorize?client_id=testClientId&redirect_uri=https://testClientId/redirect";

        when(provider.oAuthProvider()).thenReturn(OAuthProvider.GITHUB);
        when(provider.provide()).thenReturn(resultUrl);

        providers = new HashSet<>(Collections.singletonList(provider));
        composite = new AuthCodeRequestUrlProviderComposite(providers);

        String expectedUrl = composite.provide(oAuthProvider);

        assertThat(expectedUrl).isEqualTo(resultUrl);
    }

    @Test
    void provideFail() {
        when(provider.oAuthProvider()).thenReturn(OAuthProvider.KAKAO);

        providers = new HashSet<>(Collections.singletonList(provider));
        composite = new AuthCodeRequestUrlProviderComposite(providers);

        assertThatThrownBy(() -> composite.provide(OAuthProvider.GITHUB))
                .isInstanceOf(InvalidAuthException.class);
    }

}
