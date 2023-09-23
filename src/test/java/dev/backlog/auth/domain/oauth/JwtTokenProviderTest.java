package dev.backlog.auth.domain.oauth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private static final String SECRET_KEY = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

    @InjectMocks
    private JwtTokenProvider provider = new JwtTokenProvider(SECRET_KEY);

    @Test
    void generate() {
        String subject = "123L";
        Date date = new Date(System.currentTimeMillis() + 3600000);

        String result = provider.generate(subject, date);

        assertThat(result).isNotNull();
    }

    @Test
    void extractUserId() {
        String subject = "123";
        Date date = new Date(System.currentTimeMillis() + 3600000);

        String token = provider.generate(subject, date);
        Long result = provider.extractUserId(token);

        assertThat(result).isEqualTo(Long.parseLong(subject));
    }

    @Test
    void isExpiredRefreshToken() {
        String subject = "123L";
        Date date = new Date(System.currentTimeMillis() + 3600000);

        String token = provider.generate(subject, date);

        assertThat(provider.isExpiredRefreshToken(token)).isFalse();
    }

    @Test
    void isExpiredRefreshTokenFail() {
        String subject = "123L";
        Date date = DateUtil.yesterday();

        String token = provider.generate(subject, date);

        assertThat(provider.isExpiredRefreshToken(token)).isTrue();
    }

}
