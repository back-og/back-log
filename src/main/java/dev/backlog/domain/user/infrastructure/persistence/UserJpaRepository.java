package dev.backlog.domain.user.infrastructure.persistence;

import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthProviderIdAndOauthProvider(String oauthProviderId, OAuthProvider oauthProvider);

    Optional<User> findByNickname(String nickname);

}
