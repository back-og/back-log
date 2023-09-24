package dev.backlog.series.service.query;

import dev.backlog.common.dto.SliceResponse;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.dto.SeriesSummaryResponse;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.시리즈_모음;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class SeriesQueryServiceTest {

    @Autowired
    private SeriesQueryService seriesQueryService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("사용자 이름으로 시리즈 전체를 조회할 수 있다.")
    @Test
    void findSeriesTest() {
        User user = 유저();
        userRepository.save(user);
        List<Series> series = 시리즈_모음(user);
        seriesRepository.saveAll(series);

        PageRequest pageRequest = PageRequest.of(0, 10);

        SliceResponse<SeriesSummaryResponse> sliceResponse = seriesQueryService.findSeries(user.getNickname(), pageRequest);

        assertAll(
                () -> assertThat(sliceResponse.hasNext()).isFalse(),
                () -> assertThat(sliceResponse.numberOfElements()).isEqualTo(sliceResponse.data().size())
        );
    }

}
