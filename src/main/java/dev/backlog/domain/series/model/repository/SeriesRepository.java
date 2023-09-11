package dev.backlog.domain.series.model.repository;

import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.model.User;

import java.util.List;

public interface SeriesRepository {

    Series save(Series series);

    Series getByUserAndName(User user, String name);

    void deleteAll();

    List<Series> findAll();
}
