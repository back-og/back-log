package dev.backlog.domain.series.service;

import dev.backlog.domain.series.dto.SeriesCreateRequest;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.series.model.repository.SeriesRepository;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.model.User;
import dev.backlog.domain.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;

    public Long create(SeriesCreateRequest seriesCreateRequest, AuthInfo authInfo) {
        User user = userRepository.getById(authInfo.userId());
        Series series = seriesCreateRequest.toEntity(user);
        return seriesRepository.save(series).getId();
    }

}
