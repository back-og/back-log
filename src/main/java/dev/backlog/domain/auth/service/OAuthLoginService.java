package dev.backlog.domain.auth.service;

import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoLoginParams;
import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoSignUpParams;
import dev.backlog.domain.auth.model.AuthTokens;
import dev.backlog.domain.auth.model.AuthTokensGenerator;
import dev.backlog.domain.auth.model.oauth.RequestOAuthInfoService;
import dev.backlog.domain.auth.model.oauth.dto.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.dto.OAuthLoginAndSignUpParams;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static dev.backlog.domain.user.model.User.checkBlogTitle;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthLoginService {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "기본 프로필 사진 URL";

    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserService userService;

    @Transactional
    public AuthTokens kakaoLogin(KakaoLoginParams params) {
        OAuthLoginAndSignUpParams oauthParams = new OAuthLoginAndSignUpParams(params.getAuthorizationCode(), params.getOauthProvider());
        OAuthInfoResponse response = requestOAuthInfoService.request(oauthParams);
        User user = findLoginUser(response);
        return authTokensGenerator.generate(user.getId());
    }

    @Transactional
    public AuthTokens kakaoSignUp(KakaoSignUpParams params) {
        OAuthLoginAndSignUpParams oauthParams = new OAuthLoginAndSignUpParams(params.getAuthorizationCode(), params.getOauthProvider());
        OAuthInfoResponse response = requestOAuthInfoService.request(oauthParams);
        Optional<User> findUser = userRepository.findByEmail(response.email());
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }
        User user = createUser(response, params.getBlogTitle(), params.getIntroduction());
        return authTokensGenerator.generate(user.getId());
    }

    private User findLoginUser(OAuthInfoResponse response) {
        return userRepository.findByEmail(response.email())
                .orElseThrow(() -> new IllegalArgumentException("회원가입을 먼저 진행해 주세요."));
    }

    private User createUser(OAuthInfoResponse response, String blogTitle, String introduction) {
        userService.checkUser(response.oauthProviderId().toString(), response.oauthProvider());
        String profileImage = Optional.ofNullable(response.profileImage()).orElse(DEFAULT_PROFILE_IMAGE_URL);
        String checkedBlogTitle = checkBlogTitle(blogTitle, response.nickname());
        User user = User.builder()
                .oauthProvider(response.oauthProvider())
                .oauthProviderId(response.oauthProviderId().toString())
                .nickname(response.nickname())
                .email(response.email())
                .profileImage(profileImage)
                .introduction(introduction)
                .blogTitle(checkedBlogTitle)
                .build();

        return userRepository.save(user);
    }

}
