package dev.backlog.series.service;

import dev.backlog.common.exception.NotFoundException;
import dev.backlog.common.fixture.DtoFixture;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesUpdateRequest;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.backlog.common.fixture.EntityFixture.시리즈;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SeriesServiceTest {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @AfterEach
    void tearDown() {
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("시리즈를 생성할 수 있다.")
    @Test
    void createTest() {
        //given
        User user = 유저();
        userRepository.save(user);
        AuthInfo authInfo = new AuthInfo(user.getId(), "refreshToken");
        SeriesCreateRequest seriesCreateRequest = DtoFixture.시리즈_생성_요청();

        //when
        Long seriesId = seriesService.create(seriesCreateRequest, authInfo);

        //then
        assertThat(seriesId).isNotNull();
    }

    @DisplayName("시리즈를 수정할 수 있다.")
    @Test
    void updateSeriesTest() {
        //given
        User user = 유저();
        userRepository.save(user);
        AuthInfo authInfo = new AuthInfo(user.getId(), "refreshToken");
        Series series = seriesRepository.save(시리즈(user));
        SeriesUpdateRequest seriesUpdateRequest = DtoFixture.시리즈_수정_요청();

        //when
        seriesService.updateSeries(authInfo, series.getId(), seriesUpdateRequest);

        //then
        Series updatedSeries = seriesRepository.getById(series.getId());
        assertThat(updatedSeries.getName()).isEqualTo(seriesUpdateRequest.seriesName());
    }

    @DisplayName("시리즈를 삭제할 수 있다.")
    @Test
    void deleteSeriesTest() {
        //given
        User user = 유저();
        userRepository.save(user);
        AuthInfo authInfo = new AuthInfo(user.getId(), "refreshToken");
        Series series = seriesRepository.save(시리즈(user));

        //when
        seriesService.deleteSeries(authInfo, series.getId());

        //then
        Long seriesId = series.getId();
        assertThatThrownBy(() -> seriesRepository.getById(seriesId))
                .isInstanceOf(NotFoundException.class);
    }

}
