package dev.backlog.domain.series.model.repository;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.model.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.backlog.common.fixture.EntityFixture.시리즈1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;


class SeriesRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저와 시리즈의 이름으로 시리즈를 조회할 수 있다.")
    @Test
    void getByUserAndNameTest() {
        //given
        User savedUser = userRepository.save(유저1());
        Series savedSeries = seriesRepository.save(시리즈1(savedUser));

        //when
        Series foundSeries = seriesRepository.getByUserAndName(savedUser, savedSeries.getName());

        //then
        assertThat(savedSeries).isEqualTo(foundSeries);
    }

    @DisplayName("시리즈 전체를 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        //given
        User savedUser = userRepository.save(유저1());
        Series savedSeries = seriesRepository.save(시리즈1(savedUser));

        //when
        seriesRepository.deleteAll();

        //then
        String seriesName = savedSeries.getName();
        Assertions.assertThatThrownBy(() -> seriesRepository.getByUserAndName(savedUser, seriesName))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
