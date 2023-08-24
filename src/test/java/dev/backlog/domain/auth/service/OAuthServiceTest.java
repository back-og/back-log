package dev.backlog.domain.auth.service;

import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoLoginParams;
import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoSignUpParams;
import dev.backlog.domain.auth.model.AuthTokens;
import dev.backlog.domain.auth.model.AuthTokensGenerator;
import dev.backlog.domain.auth.model.oauth.dto.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.dto.OAuthLoginAndSignUpParams;
import dev.backlog.domain.auth.model.oauth.RequestOAuthInfoService;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.Email;
import dev.backlog.domain.user.model.OAuthProvider;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.service.UserService;
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
    private UserService userService;

    @Mock
    private RestTemplate restTemplate;

    private OAuthLoginService oAuthLoginService;

    private OAuthLogoutService oAuthLogoutService;

    @BeforeEach
    void setUp() {
        oAuthLoginService = new OAuthLoginService(userRepository, authTokensGenerator, requestOAuthInfoService, userService);
        oAuthLogoutService = new OAuthLogoutService(restTemplate);
        ReflectionTestUtils.setField(oAuthLogoutService, "apiUrl", "http://test-logout-api-url");
    }

    private static final String AUTHORIZATION_CODE = "authorizationCode";
    private static final String BLOG_TITLE = "블로그제목제목";
    private static final String INTRODUCTION = "안녕하세요소개입니다안녕하세요소개입니다";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String BEARER = "Bearer";
    private static final Long EXPIRES_IN = 3600L;
    private static final String NICKNAME = "닉네임";
    private static final String PROFILE_IMAGE = "프로필 사진";
    private static final String EMAIL = "email123@gmail.com";
    private static final Long OAUTH_PROVIDER_ID = 123L;
    private static final String ACCESS_TOKEN_FOR_SIGNUP = "accessTokenForSignUp";
    private static final String REFRESH_TOKEN_FOR_SIGNUP = "refreshTokenForSignUp";
    private static final String ACCESS_TOKEN_FOR_LOGIN = "accessTokenForLogin";
    private static final String REFRESH_TOKEN_FOR_LOGIN = "refreshTokenForLogin";

    @Test
    @DisplayName("카카오 소셜 회원가입에 성공한다.")
    void kakaoSignUpTest() {
        KakaoSignUpParams params = new KakaoSignUpParams(AUTHORIZATION_CODE, BLOG_TITLE, INTRODUCTION);
        OAuthInfoResponse response = new OAuthInfoResponse(NICKNAME, PROFILE_IMAGE, new Email(EMAIL), OAUTH_PROVIDER_ID, OAuthProvider.KAKAO);
        User user = User.builder()
                .oauthProvider(OAuthProvider.KAKAO)
                .oauthProviderId(response.oauthProviderId().toString())
                .nickname(response.nickname())
                .email(response.email())
                .profileImage(response.profileImage())
                .introduction(params.getIntroduction())
                .blogTitle(params.getBlogTitle())
                .build();
        AuthTokens authTokens = AuthTokens.of(ACCESS_TOKEN, REFRESH_TOKEN, BEARER, EXPIRES_IN);

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
        KakaoSignUpParams signUpParams = new KakaoSignUpParams(AUTHORIZATION_CODE, BLOG_TITLE, INTRODUCTION);
        OAuthLoginAndSignUpParams oAuthSignUpParams = new OAuthLoginAndSignUpParams(signUpParams.getAuthorizationCode(), signUpParams.getOauthProvider());
        OAuthInfoResponse signUpResponse = new OAuthInfoResponse(NICKNAME, PROFILE_IMAGE, new Email(EMAIL), OAUTH_PROVIDER_ID, OAuthProvider.KAKAO);

        User user = User.builder()
                .oauthProvider(OAuthProvider.KAKAO)
                .oauthProviderId(signUpResponse.oauthProviderId().toString())
                .nickname(signUpResponse.nickname())
                .email(signUpResponse.email())
                .profileImage(signUpResponse.profileImage())
                .introduction(signUpParams.getIntroduction())
                .blogTitle(signUpParams.getBlogTitle())
                .build();
        AuthTokens authTokensAfterSignUp = AuthTokens.of(ACCESS_TOKEN, REFRESH_TOKEN, BEARER, EXPIRES_IN);

        when(requestOAuthInfoService.request(eq(oAuthSignUpParams))).thenReturn(signUpResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterSignUp);

        AuthTokens resultOfSignUp = oAuthLoginService.kakaoSignUp(signUpParams);

        assertThat(authTokensAfterSignUp).isEqualTo(resultOfSignUp);

        /* 로그인 */
        KakaoLoginParams loginParams = new KakaoLoginParams(AUTHORIZATION_CODE);
        OAuthLoginAndSignUpParams oAuthLoginParams = new OAuthLoginAndSignUpParams(loginParams.getAuthorizationCode(), loginParams.getOauthProvider());
        OAuthInfoResponse loginResponse = new OAuthInfoResponse(NICKNAME, PROFILE_IMAGE, new Email(EMAIL), OAUTH_PROVIDER_ID, OAuthProvider.KAKAO);
        AuthTokens authTokensAfterLogin = AuthTokens.of(ACCESS_TOKEN, REFRESH_TOKEN, BEARER, EXPIRES_IN);

        when(requestOAuthInfoService.request(eq(oAuthLoginParams))).thenReturn(loginResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterLogin);

        AuthTokens resultOfLogin = oAuthLoginService.kakaoLogin(loginParams);

        assertThat(authTokensAfterLogin).isEqualTo(resultOfLogin);
    }

    @Test
    @DisplayName("카카오 소셜 회원가입 되지 않은 사용자가 로그인을 시도하면 예외가 발생한다.")
    void kakaoLoginFailTest() {
        KakaoLoginParams params = new KakaoLoginParams(AUTHORIZATION_CODE);
        OAuthInfoResponse response = new OAuthInfoResponse(NICKNAME, PROFILE_IMAGE, new Email(EMAIL), OAUTH_PROVIDER_ID, OAuthProvider.KAKAO);

        when(requestOAuthInfoService.request(any())).thenReturn(response);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oAuthLoginService.kakaoLogin(params))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("카카오 소셜 회원가입 된 사용자는 로그아웃 후 재로그인에 성공한다.")
    void kakaoLogoutTest() {
        /* 회원가입 */
        KakaoSignUpParams signUpParams = new KakaoSignUpParams(AUTHORIZATION_CODE, BLOG_TITLE, INTRODUCTION);
        OAuthLoginAndSignUpParams oAuthSignUpParams = new OAuthLoginAndSignUpParams(signUpParams.getAuthorizationCode(), signUpParams.getOauthProvider());
        OAuthInfoResponse signUpResponse = new OAuthInfoResponse(NICKNAME, PROFILE_IMAGE, new Email(EMAIL), OAUTH_PROVIDER_ID, OAuthProvider.KAKAO);

        User user = User.builder()
                .oauthProvider(OAuthProvider.KAKAO)
                .oauthProviderId("123L")
                .nickname("닉네임")
                .email(new Email("email123@gmail.com"))
                .profileImage("프로필 사진")
                .introduction(signUpParams.getIntroduction())
                .blogTitle(signUpParams.getBlogTitle())
                .build();
        AuthTokens authTokensAfterSignUp = AuthTokens.of(ACCESS_TOKEN_FOR_SIGNUP, REFRESH_TOKEN_FOR_SIGNUP, BEARER, EXPIRES_IN);

        when(requestOAuthInfoService.request(eq(oAuthSignUpParams))).thenReturn(signUpResponse);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(authTokensGenerator.generate(any())).thenReturn(authTokensAfterSignUp);

        AuthTokens resultOfSignUp = oAuthLoginService.kakaoSignUp(signUpParams);

        assertThat(authTokensAfterSignUp).isEqualTo(resultOfSignUp);

        /* 로그인 */
        KakaoLoginParams loginParams = new KakaoLoginParams(AUTHORIZATION_CODE);
        OAuthLoginAndSignUpParams oAuthLoginParams = new OAuthLoginAndSignUpParams(loginParams.getAuthorizationCode(), loginParams.getOauthProvider());
        OAuthInfoResponse loginResponse = new OAuthInfoResponse(NICKNAME, PROFILE_IMAGE, new Email(EMAIL), OAUTH_PROVIDER_ID, OAuthProvider.KAKAO);
        AuthTokens authTokensAfterLogin = AuthTokens.of(ACCESS_TOKEN_FOR_LOGIN, REFRESH_TOKEN_FOR_LOGIN, BEARER, EXPIRES_IN);

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
