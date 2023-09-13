package dev.backlog.user.infrastructure;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.common.exception.NotFoundException;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.backlog.user.exception.UserErrorCode.USER_NOT_FOUNT;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private static final String USER_NOT_REGISTERED = "회원가입되지 않은 사용자입니다 input = %s %s";

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public boolean existByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider) {
        return userJpaRepository.existByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider);
    }

    @Override
    public List<User> saveAll(Iterable<User> user) {
        return userJpaRepository.saveAll(user);
    }

    @Override
    public User getById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUNT, String.format(USER_NOT_FOUNT.getMessage(), userId)));
    }

    @Override
    public User getByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider) {
        return userJpaRepository.findByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider)
                .orElseThrow(() -> new NotFoundException(
                        USER_NOT_FOUNT,
                        String.format(USER_NOT_REGISTERED, oauthProviderId, oauthProvider)));
    }

    @Override
    public User getByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUNT, nickname));
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }

    @Override
    public void delete(User findUser) {
        userJpaRepository.delete(findUser);
    }

}
