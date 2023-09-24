package dev.backlog.auth.dto;

import dev.backlog.auth.domain.oauth.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthTokensGeneratorTest {

    private static final Long accessTokenExpireTime = 1800L;
    private static final Long refreshTokenExpireTime = 3600L;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthTokensGenerator authTokensGenerator;

    @Test
    void generateTest() {
        authTokensGenerator = new AuthTokensGenerator(jwtTokenProvider, accessTokenExpireTime, refreshTokenExpireTime);

        Long userId = 1L;

        when(jwtTokenProvider.generate(anyString(), any(Date.class))).thenReturn(ACCESS_TOKEN, REFRESH_TOKEN);

        AuthTokens authTokens = authTokensGenerator.generate(userId);

        assertThat(authTokens.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(authTokens.refreshToken()).isEqualTo(REFRESH_TOKEN);
    }

    @Test
    void refreshJwtTokenTest() {
        authTokensGenerator = new AuthTokensGenerator(jwtTokenProvider, accessTokenExpireTime, refreshTokenExpireTime);

        Long userId = 1L;

        when(jwtTokenProvider.generate(anyString(), any(Date.class))).thenReturn(ACCESS_TOKEN);

        AuthTokens authTokens = authTokensGenerator.refreshJwtToken(userId, REFRESH_TOKEN);

        assertThat(authTokens.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(authTokens.refreshToken()).isEqualTo(REFRESH_TOKEN);
    }

}
