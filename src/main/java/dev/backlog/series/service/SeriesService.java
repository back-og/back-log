package dev.backlog.series.service;

import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.dto.SeriesCreateRequest;
import dev.backlog.series.dto.SeriesUpdateRequest;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
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

    public void updateSeries(AuthInfo authInfo, Long seriesId, SeriesUpdateRequest seriesUpdateRequest) {
        User user = userRepository.getById(authInfo.userId());
        Series series = seriesRepository.getById(seriesId);
        series.verifySeriesOwner(user);
        series.updateName(seriesUpdateRequest.seriesName());
    }

    public void deleteSeries(AuthInfo authInfo, Long seriesId) {
        User user = userRepository.getById(authInfo.userId());
        Series series = seriesRepository.getById(seriesId);
        series.verifySeriesOwner(user);
        seriesRepository.delete(series);
    }

}
