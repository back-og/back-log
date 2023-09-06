package dev.backlog.domain.auth.service;

import dev.backlog.common.fixture.DtoFixture;
import dev.backlog.common.fixture.EntityFixture;
import dev.backlog.domain.auth.AuthTokens;
import dev.backlog.domain.auth.AuthTokensGenerator;
import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.auth.model.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import dev.backlog.domain.auth.model.oauth.client.OAuthMemberClientComposite;
import dev.backlog.domain.auth.model.oauth.dto.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.dto.SignupRequest;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @InjectMocks
    private OAuthService oAuthService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

    @Mock
    private OAuthMemberClientComposite oAuthMemberClientComposite;

    @Mock
    private AuthTokensGenerator authTokensGenerator;

    @DisplayName("로그인 타입에 맞는 리다이렉트 할 url을 생성한다.")
    @Test
    void getAuthCodeReqeustUrlTest() {
        String expectedUrl = "https://example.com";
        when(authCodeRequestUrlProviderComposite.provide(OAuthProvider.KAKAO)).thenReturn(expectedUrl);

        String result = oAuthService.getAuthCodeRequestUrl(OAuthProvider.KAKAO);
        assertThat(expectedUrl).isEqualTo(result);
    }

    @DisplayName("회원가입에 성공하면 토큰을 생성해 반환한다.")
    @Test
    void signupTest() {
        SignupRequest signupRequest = DtoFixture.회원가입정보();
        AuthTokens authToken = DtoFixture.토큰생성();
        User newUser = EntityFixture.유저1();
        OAuthInfoResponse response = createOAuthInfoResponse(newUser);

        when(oAuthMemberClientComposite.fetch(signupRequest.oAuthProvider(), signupRequest.authCode())).thenReturn(response);
        when(userJpaRepository.save(any())).thenReturn(newUser);
        when(authTokensGenerator.generate(newUser.getId())).thenReturn(authToken);

        AuthTokens authTokens = oAuthService.signup(signupRequest);

        assertAll(
                () -> assertThat(authTokens.accessToken()).isEqualTo("accessToken"),
                () -> assertThat(authTokens.refreshToken()).isEqualTo("refreshToken"),
                () -> assertThat(authTokens.grantType()).isEqualTo("Bearer "),
                () -> assertThat(authTokens.expiresIn()).isEqualTo(1000L)
        );
    }

    @DisplayName("로그인에 성공하면 토큰을 생성해 반환한다.")
    @Test
    void loginTest() {
        User user = EntityFixture.유저1();
        OAuthInfoResponse response = createOAuthInfoResponse(user);
        AuthTokens expectedToken = DtoFixture.토큰생성();

        when(oAuthMemberClientComposite.fetch(any(), any())).thenReturn(response);
        when(userJpaRepository.findByOauthProviderIdAndOauthProvider(user.getOauthProviderId(), user.getOauthProvider())).thenReturn(Optional.of(user));
        when(authTokensGenerator.generate(user.getId())).thenReturn(expectedToken);

        AuthTokens authTokens = oAuthService.login(OAuthProvider.KAKAO, "authCode");

        assertAll(
                () -> assertThat(authTokens.accessToken()).isEqualTo("accessToken"),
                () -> assertThat(authTokens.refreshToken()).isEqualTo("refreshToken"),
                () -> assertThat(authTokens.grantType()).isEqualTo("Bearer "),
                () -> assertThat(authTokens.expiresIn()).isEqualTo(1000L)
        );
    }

    private OAuthInfoResponse createOAuthInfoResponse(User user) {
        return OAuthInfoResponse.of(
                user.getNickname(),
                user.getProfileImage(),
                user.getEmail(),
                user.getOauthProvider(),
                user.getOauthProviderId()
        );
    }

}
