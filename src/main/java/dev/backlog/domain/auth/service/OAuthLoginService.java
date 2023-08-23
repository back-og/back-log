package dev.backlog.domain.auth.service;

import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoLoginParams;
import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoSignUpParams;
import dev.backlog.domain.auth.model.AuthTokens;
import dev.backlog.domain.auth.model.AuthTokensGenerator;
import dev.backlog.domain.auth.model.oauth.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.OAuthLoginAndSignUpParams;
import dev.backlog.domain.auth.model.oauth.RequestOAuthInfoService;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.OAuthProvider;
import dev.backlog.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OAuthLoginService {

    private static final String DEFAULT_BLOG_TITLE = ".log";
    private static final String DEFAULT_PROFILE_IMAGE_URL = "기본 프로필 사진 URL";

    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens kakaoLogin(KakaoLoginParams params) {
        OAuthLoginAndSignUpParams oauthParams = new OAuthLoginAndSignUpParams(params.getAuthorizationCode(), params.getOauthProvider());
        OAuthInfoResponse response = requestOAuthInfoService.request(oauthParams);
        Long userId = findUser(response);
        return authTokensGenerator.generate(userId);
    }

    public AuthTokens kakaoSignUp(KakaoSignUpParams params) {
        OAuthLoginAndSignUpParams oauthParams = new OAuthLoginAndSignUpParams(params.getAuthorizationCode(), params.getOauthProvider());
        OAuthInfoResponse response = requestOAuthInfoService.request(oauthParams);
        Optional<User> findUser = userRepository.findByEmail(response.email());
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }
        User user = createUser(response, params.getBlogTitle());
        return authTokensGenerator.generate(user.getId());
    }

    private Long findUser(OAuthInfoResponse response) {
        return userRepository.findByEmail(response.email())
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("회원가입을 먼저 진행해 주세요."));
    }

    private User createUser(OAuthInfoResponse response, String blogTitle) {
        checkUser(response.oauthProviderId(), response.oauthProvider());
        String profileImage = Optional.ofNullable(response.profileImage()).orElse(DEFAULT_PROFILE_IMAGE_URL);
        String checkedBlogTitle = checkBlogTitle(blogTitle, response.nickname());

        User user = new User(
                response.nickname(),
                response.email(),
                profileImage,
                checkedBlogTitle,
                response.oauthProviderId(),
                response.oauthProvider()
        );
        return userRepository.save(user);
    }

    private void checkUser(Long oauthProviderId, OAuthProvider oauthProvider) {
        Optional<User> findUser = userRepository.findByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider);
        if (findUser.isPresent() && Objects.equals(findUser.get().getOauthProviderId(), oauthProviderId)) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }
    }

    private String checkBlogTitle(String blogTitle, String nickname) {
        if (Objects.isNull(blogTitle) || blogTitle.isEmpty()) {
            return nickname + DEFAULT_BLOG_TITLE;
        }
        return blogTitle;
    }

}
