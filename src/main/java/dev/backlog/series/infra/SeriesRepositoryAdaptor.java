package dev.backlog.series.infra;

import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeriesRepositoryAdaptor implements SeriesRepository {

    private final SeriesJpaRepository seriesJpaRepository;

    @Override
    public Series save(Series series) {
        return seriesJpaRepository.save(series);
    }

    @Override
    public Series getByUserAndName(User user, String name) {
        return seriesJpaRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    @Override
    public void deleteAll() {
        seriesJpaRepository.deleteAll();
    }

    @Override
    public List<Series> findAll() {
        return seriesJpaRepository.findAll();
    }

}
