package dev.backlog.domain.series.infrastructure.persistence;

import dev.backlog.common.RepositoryTest;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.Email;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.backlog.domain.auth.model.oauth.OAuthProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

class SeriesJpaRepositoryTest extends RepositoryTest {

    @Autowired
    private SeriesJpaRepository seriesJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @DisplayName("유저와 시리즈의 이름으로 시리즈를 조회할 수 있다.")
    @Test
    void findByUserAndNameTest() {
        User user = User.builder()
                .oauthProvider(KAKAO)
                .oauthProviderId("test")
                .nickname("test")
                .email(new Email("test@example.com"))
                .profileImage("image")
                .blogTitle("blogTitle")
                .build();
        User savedUser = userJpaRepository.save(user);

        Series series = Series.builder()
                .user(savedUser)
                .name("시리즈")
                .build();
        Series savedSeries = seriesJpaRepository.save(series);

        Series foundSeries = seriesJpaRepository.findByUserAndName(savedUser, "시리즈").get();
        assertThat(savedSeries).isEqualTo(foundSeries);
    }

}
