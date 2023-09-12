package dev.backlog.domain.user.model.repository;

import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.user.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User getById(Long userId);

    User getByNickname(String nickname);

    User getByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider);

    List<User> saveAll(Iterable<User> user);

    void deleteAll();

    void delete(User findUser);

}
