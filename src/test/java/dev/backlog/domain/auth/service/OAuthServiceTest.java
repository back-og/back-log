package dev.backlog.domain.auth.service;

import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoLoginParams;
import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoSignUpParams;
import dev.backlog.domain.auth.model.AuthTokens;
import dev.backlog.domain.auth.model.AuthTokensGenerator;
import dev.backlog.domain.auth.model.oauth.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.OAuthLoginAndSignUpParams;
import dev.backlog.domain.auth.model.oauth.RequestOAuthInfoService;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.Email;
import dev.backlog.domain.user.model.OAuthProvider;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthTokensGenerator authTokensGenerator;

    @Mock
    private RequestOAuthInfoService requestOAuthInfoService;

    @Mock
    private RestTemplate restTemplate;

    private OAuthLoginService oAuthLoginService;

    private OAuthLogoutService oAuthLogoutService;

    @BeforeEach
    void setUp() {
        oAuthLoginService = new OAuthLoginService(userRepository, authTokensGenerator, requestOAuthInfoService);
        oAuthLogoutService = new OAuthLogoutService(restTemplate);
        ReflectionTestUtils.setField(oAuthLogoutService, "apiUrl", "http://test-logout-api-url");
    }

    @Test
    @DisplayName("카카오 소셜 회원가입에 성공한다.")
    void kakaoSignUpTest() {
        KakaoSignUpParams params = new KakaoSignUpParams("authorizationCode", "블로그제목제목", "안녕하세요소개입니다안녕하세요소개입니다");
        OAuthInfoResponse response = new OAuthInfoResponse("닉네임", "프로필 사진", new Email("email123@gmail.com"), 123L, OAuthProvider.KAKAO);
        User user = User.builder()
                .oauthProvider(OAuthProvider.KAKAO)
                .oauthProviderId("123L")
                .nickname("닉네임")
                .email(new Email("email123@gmail.com"))
                .profileImage("프로필 사진")
                .introduction(params.getIntroduction())
                .blogTitle(params.getBlogTitle())
                .build();
        AuthTokens authTokens = AuthTokens.of("accessToken", "refreshToken", "Bearer", 3600L);

        when(requestOAuthInfoService.request(any())).thenReturn(response);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(authTokensGenerator.generate(any())).thenReturn(authTokens);

        AuthTokens result = oAuthLoginService.kakaoSignUp(params);

        assertThat(authTokens).isEqualTo(result);
    }

    @Test
    @DisplayName("카카오 소셜 회원가입 후 사용자는 로그인에 성공한다.")
    void kakaoLoginTest() {
        /* 회원가입 */
        KakaoSignUpParams signUpParams = new KakaoSignUpParams("authorizationCode", "블로그제목제목", "안녕하세요소개입니다안녕하세요소개입니다");
        OAuthLoginAndSignUpParams oAuthSignUpParams = new OAuthLoginAndSignUpParams(signUpParams.getAuthorizationCode(), signUpParams.getOauthProvider());
        OAuthInfoResponse signUpResponse = new OAuthInfoResponse("닉네임", "프로필 사진", new Email("email123@gmail.com"), 123L, OAuthProvider.KAKAO);
        User user = User.builder()
                .oauthProvider(OAuthProvider.KAKAO)
                .oauthProviderId("123L")
                .nickname("닉네임")
                .email(new Email("email123@gmail.com"))
                .profileImage("프로필 사진")
                .introduction(signUpParams.getIntroduction())
                .blogTitle(signUpParams.getBlogTitle())
                .build();
        AuthTokens authTokensAfterSignUp = AuthTokens.of("accessTokenForSignUp", "refreshTokenForSignUp", "Bearer", 3600L);

        when(requestOAuthInfoService.request(eq(oAuthSignUpParams))).thenReturn(signUpResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterSignUp);

        AuthTokens resultOfSignUp = oAuthLoginService.kakaoSignUp(signUpParams);

        assertThat(authTokensAfterSignUp).isEqualTo(resultOfSignUp);

        /* 로그인 */
        KakaoLoginParams loginParams = new KakaoLoginParams("authorizationCode");
        OAuthLoginAndSignUpParams oAuthLoginParams = new OAuthLoginAndSignUpParams(loginParams.getAuthorizationCode(), loginParams.getOauthProvider());
        OAuthInfoResponse loginResponse = new OAuthInfoResponse("닉네임", "프로필 사진", new Email("email123@gmail.com"), 123L, OAuthProvider.KAKAO);
        AuthTokens authTokensAfterLogin = AuthTokens.of("accessTokenForLogin", "refreshTokenForLogin", "Bearer", 3600L);

        when(requestOAuthInfoService.request(eq(oAuthLoginParams))).thenReturn(loginResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterLogin);

        AuthTokens resultOfLogin = oAuthLoginService.kakaoLogin(loginParams);

        assertThat(authTokensAfterLogin).isEqualTo(resultOfLogin);
    }

    @Test
    @DisplayName("카카오 소셜 회원가입 되지 않은 사용자가 로그인을 시도하면 예외가 발생한다.")
    void kakaoLoginFailTest() {
        KakaoLoginParams params = new KakaoLoginParams("authorizationCode");
        OAuthInfoResponse response = new OAuthInfoResponse("닉네임", "프로필 사진", new Email("email123@gmail.com"), 123L, OAuthProvider.KAKAO);

        when(requestOAuthInfoService.request(any())).thenReturn(response);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oAuthLoginService.kakaoLogin(params))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("카카오 소셜 회원가입 된 사용자는 로그아웃 후 재로그인에 성공한다.")
    void kakaoLogoutTest() {
        /* 회원가입 */
        KakaoSignUpParams signUpParams = new KakaoSignUpParams("authorizationCode", "블로그제목제목", "안녕하세요소개입니다안녕하세요소개입니다");
        OAuthLoginAndSignUpParams oAuthSignUpParams = new OAuthLoginAndSignUpParams(signUpParams.getAuthorizationCode(), signUpParams.getOauthProvider());
        OAuthInfoResponse signUpResponse = new OAuthInfoResponse("닉네임", "프로필 사진", new Email("email123@gmail.com"), 123L, OAuthProvider.KAKAO);
        User user = User.builder()
                .oauthProvider(OAuthProvider.KAKAO)
                .oauthProviderId("123L")
                .nickname("닉네임")
                .email(new Email("email123@gmail.com"))
                .profileImage("프로필 사진")
                .introduction(signUpParams.getIntroduction())
                .blogTitle(signUpParams.getBlogTitle())
                .build();
        AuthTokens authTokensAfterSignUp = AuthTokens.of("accessTokenForSignUp", "refreshTokenForSignUp", "Bearer", 3600L);

        when(requestOAuthInfoService.request(eq(oAuthSignUpParams))).thenReturn(signUpResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterSignUp);

        AuthTokens resultOfSignUp = oAuthLoginService.kakaoSignUp(signUpParams);

        assertThat(authTokensAfterSignUp).isEqualTo(resultOfSignUp);

        /* 로그인 */
        KakaoLoginParams loginParams = new KakaoLoginParams("authorizationCode");
        OAuthLoginAndSignUpParams oAuthLoginParams = new OAuthLoginAndSignUpParams(loginParams.getAuthorizationCode(), loginParams.getOauthProvider());
        OAuthInfoResponse loginResponse = new OAuthInfoResponse("닉네임", "프로필 사진", new Email("email123@gmail.com"), 123L, OAuthProvider.KAKAO);
        AuthTokens authTokensAfterLogin = AuthTokens.of("accessTokenForLogin", "refreshTokenForLogin", "Bearer", 3600L);

        when(requestOAuthInfoService.request(eq(oAuthLoginParams))).thenReturn(loginResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterLogin);

        AuthTokens resultOfLogin = oAuthLoginService.kakaoLogin(loginParams);

        assertThat(authTokensAfterLogin).isEqualTo(resultOfLogin);

        /* 로그아웃 */
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        oAuthLogoutService.kakaoLogout(authTokensAfterLogin.getAccessToken());

        /* 재로그인 */
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        AuthTokens resultOfReLogin = oAuthLoginService.kakaoLogin(loginParams);

        assertThat(resultOfReLogin).isNotNull();
    }

}
