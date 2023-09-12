package dev.backlog.user.domain.repository;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.user.domain.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    boolean existsByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider);

    User getById(Long userId);

    User getByNickname(String nickname);

    User getByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider);

    List<User> saveAll(Iterable<User> user);

    void deleteAll();

    void delete(User findUser);

}
