package dev.backlog.domain.auth.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1800000;
    private static final String USER_ID = "1";

    @Test
    void jwtTokenProvideTest() {
        String accessToken = jwtTokenProvider.generate(USER_ID, new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME));

        Long userId = jwtTokenProvider.extractUserId(accessToken);

        System.out.println("accessToken : " + accessToken);
        System.out.println("userId : " + userId);
    }
}
