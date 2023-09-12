package dev.backlog.series.service;

import dev.backlog.common.fixture.DtoFixture;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.backlog.common.fixture.EntityFixture.유저1;

@SpringBootTest
class SeriesServiceTest {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("시리즈를 생성할 수 있다.")
    @Test
    void createTest() {
        //given
        User user = 유저1();
        userRepository.save(user);
        AuthInfo authInfo = new AuthInfo(user.getId(), "refreshToken");
        SeriesCreateRequest seriesCreateRequest = DtoFixture.시리즈생성요청();

        //when
        Long seriesId = seriesService.create(seriesCreateRequest, authInfo);

        //then
        Assertions.assertThat(seriesId).isNotNull();
    }

}
