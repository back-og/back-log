package dev.backlog.user.infrastructure;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.common.exception.NotFoundException;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.backlog.user.exception.UserErrorCode.USER_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private static final String USER_NOT_REGISTERED_MESSAGE = "해당 아이디(%s)와 로그인 타입(%s)의 유저는 회원가입되지 않았습니다.";
    private static final String USER_NOT_FOUND_MESSAGE = "해당 아이디(%s)로 유저를 찾을 수 없습니다.";

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public boolean existsByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider) {
        return userJpaRepository.existsByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider);
    }

    @Override
    public List<User> saveAll(Iterable<User> user) {
        return userJpaRepository.saveAll(user);
    }

    @Override
    public User getById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                                USER_NOT_FOUND,
                                String.format(USER_NOT_FOUND_MESSAGE, userId)
                        )
                );
    }

    @Override
    public User getByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider) {
        return userJpaRepository.findByOauthProviderIdAndOauthProvider(oauthProviderId, oauthProvider)
                .orElseThrow(() -> new NotFoundException(
                                USER_NOT_FOUND,
                                String.format(USER_NOT_REGISTERED_MESSAGE, oauthProviderId, oauthProvider)
                        )
                );
    }

    @Override
    public User getByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new NotFoundException(
                                USER_NOT_FOUND,
                                String.format(USER_NOT_FOUND_MESSAGE, nickname)
                        )
                );
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
