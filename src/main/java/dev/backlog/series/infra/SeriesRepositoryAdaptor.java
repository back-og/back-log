package dev.backlog.series.infra;

import dev.backlog.common.exception.NotFoundException;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.exception.SeriesErrorCode;
import dev.backlog.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeriesRepositoryAdaptor implements SeriesRepository {

    private static final String USER_AND_SERIES_NOT_FOUND_MESSAGE = "유저 아이디(%s)와 시리즈 이름(%s)에 대한 시리즈가 없습니다.";
    private static final String SERIES_NOT_FOUND_MESSAGE = "시리즈 아이디(%s)에 대한 시리즈가 없습니다.";

    private final SeriesJpaRepository seriesJpaRepository;

    @Override
    public Series save(Series series) {
        return seriesJpaRepository.save(series);
    }

    @Override
    public List<Series> saveAll(Iterable<Series> series) {
        return seriesJpaRepository.saveAll(series);
    }

    @Override
    public Series getById(Long seriesId) {
        return seriesJpaRepository.findById(seriesId)
                .orElseThrow(() -> new NotFoundException(
                        SeriesErrorCode.SERIES_NOT_FOUNT,
                        String.format(SERIES_NOT_FOUND_MESSAGE, seriesId)));
    }

    @Override
    public Series getByUserAndName(User user, String name) {
        return seriesJpaRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new NotFoundException(
                        SeriesErrorCode.SERIES_NOT_FOUNT,
                        String.format(USER_AND_SERIES_NOT_FOUND_MESSAGE, user.getId(), name)));
    }

    @Override
    public void deleteAll() {
        seriesJpaRepository.deleteAll();
    }

    @Override
    public Slice<Series> findAllByUser(User user, Pageable pageable) {
        return seriesJpaRepository.findAllByUser(user, pageable);
    }

    @Override
    public List<Series> findAll() {
        return seriesJpaRepository.findAll();
    }

    @Override
    public void delete(Series series) {
        seriesJpaRepository.delete(series);
    }

}
