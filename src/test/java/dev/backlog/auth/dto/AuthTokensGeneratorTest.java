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

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthTokensGenerator authTokensGenerator;

    @Test
    void generate() {
        authTokensGenerator = new AuthTokensGenerator(jwtTokenProvider, accessTokenExpireTime, refreshTokenExpireTime);

        Long userId = 1L;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(jwtTokenProvider.generate(anyString(), any(Date.class))).thenReturn(accessToken, refreshToken);

        AuthTokens authTokens = authTokensGenerator.generate(userId);

        assertThat(authTokens.accessToken()).isEqualTo(accessToken);
        assertThat(authTokens.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void refreshJwtToken() {
        authTokensGenerator = new AuthTokensGenerator(jwtTokenProvider, accessTokenExpireTime, refreshTokenExpireTime);

        Long userId = 1L;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(jwtTokenProvider.generate(anyString(), any(Date.class))).thenReturn(accessToken);

        AuthTokens authTokens = authTokensGenerator.refreshJwtToken(userId, refreshToken);

        assertThat(authTokens.accessToken()).isEqualTo(accessToken);
        assertThat(authTokens.refreshToken()).isEqualTo(refreshToken);
    }

}