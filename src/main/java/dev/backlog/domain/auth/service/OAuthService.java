package dev.backlog.domain.auth.service;

import dev.backlog.domain.auth.AuthTokens;
import dev.backlog.domain.auth.AuthTokensGenerator;
import dev.backlog.domain.auth.model.oauth.JwtTokenProvider;
import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.auth.model.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import dev.backlog.domain.auth.model.oauth.client.OAuthMemberClientComposite;
import dev.backlog.domain.auth.model.oauth.dto.OAuthInfoResponse;
import dev.backlog.domain.auth.model.oauth.dto.SignupRequest;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OAuthMemberClientComposite oauthMemberClientComposite;
    private final UserJpaRepository userJpaRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;

    public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
        return authCodeRequestUrlProviderComposite.provide(oAuthProvider);
    }

    public AuthTokens signup(SignupRequest request) {
        OAuthInfoResponse response = oauthMemberClientComposite.fetch(request.oAuthProvider(), request.authCode());
        User user = User.builder()
                .oauthProvider(response.oAuthProvider())
                .oauthProviderId(response.oAuthProviderId())
                .nickname(response.nickname())
                .email(response.email())
                .profileImage(response.profileImage())
                .introduction(request.introduction())
                .blogTitle(request.blogTitle())
                .build();
        User newUser = userJpaRepository.save(user);

        return authTokensGenerator.generate(newUser.getId());
    }

    public AuthTokens login(OAuthProvider oauthProvider, String authCode) {
        OAuthInfoResponse response = oauthMemberClientComposite.fetch(oauthProvider, authCode);
        User findUser = userJpaRepository.findByOauthProviderIdAndOauthProvider(String.valueOf(response.oAuthProviderId()), response.oAuthProvider())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다. 회원가입을 먼저 진행해 주세요."));

        return authTokensGenerator.generate(findUser.getId());
    }

    public AuthTokens renew(Long userId, String token) {
        if (jwtTokenProvider.isExpiredRefreshToken(token)) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요.");
        } else {
            return authTokensGenerator.refreshJwtToken(userId, token);
        }
    }

}
