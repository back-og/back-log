package dev.backlog.domain.series.infra;

import dev.backlog.domain.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.series.model.repository.SeriesRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
