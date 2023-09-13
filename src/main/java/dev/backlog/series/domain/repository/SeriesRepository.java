package dev.backlog.series.domain.repository;

import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface SeriesRepository {

    Series save(Series series);

    List<Series> saveAll(Iterable<Series> series);

    Series getById(Long seriesId);

    Series getByUserAndName(User user, String name);

    List<Series> findAll();

    Slice<Series> findAllByUser(User user, Pageable pageable);

    void delete(Series series);

    void deleteAll();
}
