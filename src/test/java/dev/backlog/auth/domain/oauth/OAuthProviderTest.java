package dev.backlog.auth.domain.oauth;

import org.junit.jupiter.api.Test;

import static dev.backlog.auth.domain.oauth.OAuthProvider.GITHUB;
import static dev.backlog.auth.domain.oauth.OAuthProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

class OAuthProviderTest {

    @Test
    void fromTest() {
        String kakao = "kakao";
        OAuthProvider result = OAuthProvider.from(kakao);

        assertThat(result).isEqualTo(KAKAO);
    }

    @Test
    void valuesTest() {
        OAuthProvider[] oAuthProviders = OAuthProvider.values();

        assertThat(oAuthProviders).hasSize(2);
    }

    @Test
    void valueOfTest() {
        String github = "GITHUB";
        OAuthProvider result = OAuthProvider.valueOf(github);

        assertThat(result).isEqualTo(GITHUB);
    }

}
