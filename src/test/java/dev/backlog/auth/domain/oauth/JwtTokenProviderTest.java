package dev.backlog.auth.domain.oauth;

import io.jsonwebtoken.SignatureAlgorithm;
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

    private static final String SUBJECT = "123";
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

    @InjectMocks
    private JwtTokenProvider provider = new JwtTokenProvider(SECRET_KEY);

    @Test
    void generateTest() {
        Date date = new Date(System.currentTimeMillis() + 3600000);

        String result = provider.generate(SUBJECT, date);

        assertThat(result).isNotNull();
    }

    @Test
    void extractUserIdTest() {
        Date date = new Date(System.currentTimeMillis() + 3600000);

        String token = provider.generate(SUBJECT, date);
        Long result = provider.extractUserId(token);

        assertThat(result).isEqualTo(Long.parseLong(SUBJECT));
    }

    @Test
    void isExpiredRefreshTokenTest() {
        Date date = new Date(System.currentTimeMillis() + 3600000);

        String token = provider.generate(SUBJECT, date);

        assertThat(provider.isExpiredRefreshToken(token)).isFalse();
    }

    @Test
    void isExpiredRefreshTokenFailTest() {
        String subject = SUBJECT;
        Date date = DateUtil.yesterday();

        String token = provider.generate(subject, date);

        assertThat(provider.isExpiredRefreshToken(token)).isTrue();
    }

}
