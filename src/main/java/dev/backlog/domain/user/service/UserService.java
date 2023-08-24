package dev.backlog.domain.user.service;

import dev.backlog.domain.auth.infrastructure.JwtTokenProvider;
import dev.backlog.domain.user.dto.UserDetailsResponse;
import dev.backlog.domain.user.dto.UserResponse;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.OAuthProvider;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserResponse findUserProfile(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자는 찾을 수 없습니다."));
        return new UserResponse(user);
    }

    public UserDetailsResponse findMyProfile(String token) {
        Long userId = jwtTokenProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자는 찾을 수 없습니다."));
        return new UserDetailsResponse(user);
    }

    public void checkUser(String oauthProviderId, OAuthProvider oauthProvider) {
        Optional<User> findUser = userRepository.findByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider);
        if (findUser.isPresent() && Objects.equals(findUser.get().getOauthProviderId(), oauthProviderId)) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }
    }

}
