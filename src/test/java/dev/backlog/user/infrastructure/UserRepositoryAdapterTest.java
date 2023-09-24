package dev.backlog.user.infrastructure;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.common.exception.NotFoundException;
import dev.backlog.user.domain.User;
import dev.backlog.user.infrastructure.jpa.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    void existsByOauthProviderIdAndOauthProvider() {
        String providerId = "123";
        OAuthProvider github = OAuthProvider.GITHUB;

        when(userJpaRepository.existsByOauthProviderIdAndOauthProvider(providerId, github)).thenReturn(true);

        assertThat(adapter.existsByOauthProviderIdAndOauthProvider(providerId, github)).isTrue();
    }

    @Test
    void getByOauthProviderIdAndOauthProvider() {
        String providerId = "123";
        OAuthProvider github = OAuthProvider.GITHUB;
        User user = 유저();

        when(userJpaRepository.findByOauthProviderIdAndOauthProvider(providerId, github)).thenReturn(Optional.ofNullable(user));

        assertThat(adapter.getByOauthProviderIdAndOauthProvider(providerId, github).getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    void getByOauthProviderIdAndOauthProviderFail() {
        String providerId = "123";
        OAuthProvider github = OAuthProvider.GITHUB;

        when(userJpaRepository.findByOauthProviderIdAndOauthProvider(providerId, github)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getByOauthProviderIdAndOauthProvider(providerId, github))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getByNicknameFail() {
        String nickname = "nickname";

        when(userJpaRepository.findByNickname(nickname)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getByNickname(nickname))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete() {
        User user = 유저();
        User savedUser = userJpaRepository.save(user);

        adapter.delete(savedUser);

    }

}
