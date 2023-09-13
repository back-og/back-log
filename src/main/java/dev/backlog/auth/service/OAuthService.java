package dev.backlog.auth.service;

import dev.backlog.auth.AuthTokens;
import dev.backlog.auth.AuthTokensGenerator;
import dev.backlog.auth.domain.oauth.JwtTokenProvider;
import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import dev.backlog.auth.domain.oauth.client.OAuthMemberClientComposite;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.auth.domain.oauth.dto.SignupRequest;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OAuthMemberClientComposite oauthMemberClientComposite;
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;

    public String getAuthCodeRequestUrl(OAuthProvider oAuthProvider) {
        return authCodeRequestUrlProviderComposite.provide(oAuthProvider);
    }

    public AuthTokens signup(SignupRequest request) {
        OAuthInfoResponse response = oauthMemberClientComposite.fetch(request.oAuthProvider(), request.authCode());
        checkDuplicateUser(response);
        String profileImage = checkProfileImage(response.profileImage());

        User user = User.builder()
                .oauthProvider(response.oAuthProvider())
                .oauthProviderId(response.oAuthProviderId())
                .nickname(response.nickname())
                .email(response.email())
                .profileImage(profileImage)
                .introduction(request.introduction())
                .blogTitle(request.blogTitle())
                .build();
        User newUser = userRepository.save(user);

        return authTokensGenerator.generate(newUser.getId());
    }

    public AuthTokens login(OAuthProvider oauthProvider, String authCode) {
        OAuthInfoResponse response = oauthMemberClientComposite.fetch(oauthProvider, authCode);
        User findUser = userRepository.getByOauthProviderIdAndOauthProvider(String.valueOf(response.oAuthProviderId()), response.oAuthProvider());

        checkUserIsDeleted(findUser);
        return authTokensGenerator.generate(findUser.getId());
    }

    public AuthTokens renew(Long userId, String token) {
        if (jwtTokenProvider.isExpiredRefreshToken(token)) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요.");
        } else {
            return authTokensGenerator.refreshJwtToken(userId, token);
        }
    }

    private void checkDuplicateUser(OAuthInfoResponse response) {
        if (userRepository.existsByOauthProviderIdAndOauthProvider(response.oAuthProviderId(), response.oAuthProvider())) {
            throw new IllegalArgumentException("이미 회원 가입된 사용자입니다.");
        }
    }

    private String checkProfileImage(String profileImage) {
        if (profileImage == null) {
            profileImage = "기본 이미지 URL";
        }
        return profileImage;
    }

    private void checkUserIsDeleted(User findUser) {
        if (findUser.isDeleted() && (!Objects.equals(findUser.getDeletedDate(), LocalDate.of(9999, 12, 31)))) {
            Period between = Period.between(findUser.getDeletedDate(), LocalDate.now());
            if (between.getDays() >= 30) {
                userRepository.delete(findUser);
                throw new IllegalArgumentException("탈퇴한 지 30일이 지난 사용자입니다. 다시 회원 가입해 주세요.");
            }
            findUser.unmarkUserAsDeleted();
        }
    }

}
