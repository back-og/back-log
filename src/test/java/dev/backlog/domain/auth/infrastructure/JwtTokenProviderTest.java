package dev.backlog.domain.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1800000;
    private static final Long USER_ID = 1L;

    @DisplayName("JWT 토큰을 생성한다.")
    @Test
    void jwtTokenProvideTest() {
        String accessToken = jwtTokenProvider.generate(USER_ID.toString(), new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME));
        Long userId = jwtTokenProvider.extractUserId(accessToken);

        assertThat(accessToken).isNotNull();
        assertThat(userId).isEqualTo(USER_ID);
    }

}
