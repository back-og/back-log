package dev.backlog.series.domain.repository;

import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;

import java.util.List;

public interface SeriesRepository {

    Series save(Series series);

    Series getByUserAndName(User user, String name);

    void deleteAll();

    List<Series> findAll();

    Series getByIdAndUser(Long seriesId, User user);

}
