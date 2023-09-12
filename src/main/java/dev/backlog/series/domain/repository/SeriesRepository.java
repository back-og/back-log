package dev.backlog.series.domain.repository;

import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;

import java.util.List;

public interface SeriesRepository {

    Series save(Series series);

    Series getById(Long seriesId);

    Series getByUserAndName(User user, String name);

    List<Series> findAll();

    void delete(Series series);

    void deleteAll();

}
