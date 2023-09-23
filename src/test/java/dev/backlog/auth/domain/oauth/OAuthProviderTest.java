package dev.backlog.auth.domain.oauth;

import org.junit.jupiter.api.Test;

import static dev.backlog.auth.domain.oauth.OAuthProvider.GITHUB;
import static dev.backlog.auth.domain.oauth.OAuthProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

class OAuthProviderTest {

    @Test
    void from() {
        String kakao = "kakao";
        OAuthProvider result = OAuthProvider.from(kakao);

        assertThat(result).isEqualTo(KAKAO);
    }

    @Test
    void values() {
        OAuthProvider[] oAuthProviders = OAuthProvider.values();

        assertThat(oAuthProviders.length).isEqualTo(2);
    }

    @Test
    void valueOf() {
        String github = "GITHUB";
        OAuthProvider result = OAuthProvider.valueOf(github);

        assertThat(result).isEqualTo(GITHUB);
    }

}
