package dev.backlog.series.infra.jpa;

import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesJpaRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByUserAndName(User user, String name);

    Slice<Series> findAllByUser(User user, Pageable pageable);

}
