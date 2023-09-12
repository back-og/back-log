package dev.backlog.series.infra.jpa;

import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesJpaRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByUserAndName(User user, String name);

    Optional<Series> findByIdAndUser(Long id, User user);

}
