package dev.backlog.series.domain.repository;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.common.exception.NotFoundException;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.backlog.common.fixture.EntityFixture.시리즈;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;


class SeriesRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("유저와 시리즈의 이름으로 시리즈를 조회할 수 있다.")
    @Test
    void getByUserAndNameTest() {
        User savedUser = userRepository.save(유저());
        Series savedSeries = seriesRepository.save(시리즈(savedUser));

        Series foundSeries = seriesRepository.getByUserAndName(savedUser, savedSeries.getName());

        assertThat(savedSeries).isEqualTo(foundSeries);
    }

    @DisplayName("시리즈 전체를 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        User savedUser = userRepository.save(유저());
        Series savedSeries = seriesRepository.save(시리즈(savedUser));

        seriesRepository.deleteAll();

        String seriesName = savedSeries.getName();
        Assertions.assertThatThrownBy(() -> seriesRepository.getByUserAndName(savedUser, seriesName))
                .isInstanceOf(NotFoundException.class);

    }

}
