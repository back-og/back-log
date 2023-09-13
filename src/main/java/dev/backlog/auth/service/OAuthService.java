package dev.backlog.auth.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import dev.backlog.auth.AuthTokens;
import dev.backlog.auth.AuthTokensGenerator;
import dev.backlog.auth.domain.oauth.JwtTokenProvider;
import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import dev.backlog.auth.domain.oauth.client.OAuthMemberClientComposite;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.auth.domain.oauth.dto.SignupRequest;
import dev.backlog.common.exception.InvalidAuthException;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.backlog.auth.exception.AuthErrorCode.ALREADY_REGISTERED;
import static dev.backlog.auth.exception.AuthErrorCode.AUTHENTICATION_FAILED;
import static dev.backlog.auth.exception.AuthErrorCode.DELETED_USER;
import static dev.backlog.auth.exception.AuthErrorMessage.EXPIRED_REFRESH_TOKEN;

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

        User user = User.builder()
                .oauthProvider(response.oAuthProvider())
                .oauthProviderId(response.oAuthProviderId())
                .nickname(response.nickname())
                .email(response.email())
                .profileImage(response.profileImage())
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
            throw new InvalidAuthException(
                    AUTHENTICATION_FAILED, EXPIRED_REFRESH_TOKEN);
        } else {
            return authTokensGenerator.refreshJwtToken(userId, token);
        }
    }

    private void checkDuplicateUser(OAuthInfoResponse response) {
        if (userRepository.existsByOauthProviderIdAndOauthProvider(response.oAuthProviderId(), response.oAuthProvider())) {
            throw new InvalidAuthException(
                    ALREADY_REGISTERED, ALREADY_REGISTERED.getMessage());
        }
    }

    private void checkUserIsDeleted(User findUser) {
        if (findUser.isDeleted() && (!Objects.equals(findUser.getDeletedDate(), LocalDate.of(9999, 12, 31)))) {
            Period between = Period.between(findUser.getDeletedDate(), LocalDate.now());
            if (between.getDays() >= 30) {
                userRepository.delete(findUser);
                throw new InvalidAuthException(
                        DELETED_USER, DELETED_USER.getMessage());
            }
            findUser.unmarkUserAsDeleted();
        }
    }

}
