package dev.backlog.domain.user.infrastructure;

import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.user.infrastructure.jpa.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public List<User> saveAll(Iterable<User> user) {
        return userJpaRepository.saveAll(user);
    }

    @Override
    public User getById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자는 찾을 수 없습니다."));
    }

    @Override
    public User getByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider) {
        return userJpaRepository.findByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다. 회원가입을 먼저 진행해주세요."));
    }

    @Override
    public User getByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }

}
