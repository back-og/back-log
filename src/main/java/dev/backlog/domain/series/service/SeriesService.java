package dev.backlog.domain.series.service;

import dev.backlog.common.exception.NotFoundException;
import dev.backlog.domain.series.dto.SeriesCreateRequest;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.series.model.repository.SeriesRepository;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.backlog.domain.user.exception.UserErrorCode.USER_NOT_FOUNT;

@Service
@RequiredArgsConstructor
@Transactional
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final UserJpaRepository userJpaRepository;

    public Long create(SeriesCreateRequest seriesCreateRequest, AuthInfo authInfo) {
        // TODO: 2023/09/11 UserRepository 분리되면, 리팩토링
        User user = userJpaRepository.findById(authInfo.userId())
                .orElseThrow(() -> new NotFoundException(
                        USER_NOT_FOUNT,
                        String.format(USER_NOT_FOUNT.getMessage(), authInfo.userId())));
        Series series = seriesCreateRequest.toEntity(user);
        return seriesRepository.save(series).getId();
    }

}
