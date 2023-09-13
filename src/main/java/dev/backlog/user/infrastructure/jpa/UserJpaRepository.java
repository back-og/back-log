package dev.backlog.user.infrastructure.jpa;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    boolean existByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider);

    Optional<User> findByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider);

    Optional<User> findByNickname(String nickname);

}
