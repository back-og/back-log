package dev.backlog.series.infra;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.시리즈;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;

class SeriesRepositoryAdaptorTest extends RepositoryTestConfig {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("시리즈를 생성할 수 있다.")
    @Test
    void saveTest() {
        User user = 유저();
        userRepository.save(user);
        Series series = 시리즈(user);

        Series savedSeries = seriesRepository.save(series);

        assertThat(savedSeries).isEqualTo(series);
    }

    @DisplayName("사용자와 시리즈 이름으로 조회할 수 있다.")
    @Test
    void getByUserAndNameTest() {
        User user = 유저();
        userRepository.save(user);
        Series series = 시리즈(user);
        Series savedSeries = seriesRepository.save(series);

        Series foundSeries = seriesRepository.getByUserAndName(user, series.getName());

        assertThat(foundSeries).isEqualTo(savedSeries);
    }

    @DisplayName("시리즈를 전체 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        User user = 유저();
        userRepository.save(user);
        Series series = 시리즈(user);
        seriesRepository.save(series);

        seriesRepository.deleteAll();

        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).isEmpty();
    }

    @DisplayName("사용자별 시리즈를 조회할 수 있다.")
    @Test
    void findAllByUser() {
        User user = 유저();
        userRepository.save(user);
        List<Series> series = List.of(시리즈(user), 시리즈(user));
        seriesRepository.saveAll(series);
        PageRequest pageRequest = PageRequest.of(0, 30);

        Slice<Series> seriesSlice = seriesRepository.findAllByUser(user, pageRequest);

        assertThat(seriesSlice).containsAll(series);
    }

}
