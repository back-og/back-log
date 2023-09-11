package dev.backlog.domain.series.infra;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.series.model.repository.SeriesRepository;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.model.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.시리즈1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;

class SeriesRepositoryAdaptorTest extends RepositoryTestConfig {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("시리즈를 생성할 수 있다.")
    @Test
    void saveTest() {
        //given
        User user = 유저1();
        userRepository.save(user);
        Series series = 시리즈1(user);

        //when
        Series savedSeries = seriesRepository.save(series);

        //then
        assertThat(savedSeries).isEqualTo(series);
    }

    @DisplayName("사용자와 시리즈 이름으로 조회할 수 있다.")
    @Test
    void getByUserAndNameTest() {
        //given
        User user = 유저1();
        userRepository.save(user);
        Series series = 시리즈1(user);
        Series savedSeries = seriesRepository.save(series);

        //when
        Series foundSeries = seriesRepository.getByUserAndName(user, series.getName());

        //then
        assertThat(foundSeries).isEqualTo(savedSeries);
    }

    @DisplayName("시리즈를 전체 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        //given
        User user = 유저1();
        userRepository.save(user);
        Series series = 시리즈1(user);
        seriesRepository.save(series);

        //when
        seriesRepository.deleteAll();

        //then
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).isEmpty();
    }

}
