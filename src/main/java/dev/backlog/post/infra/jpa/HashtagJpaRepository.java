package dev.backlog.post.infra.jpa;

import dev.backlog.post.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagJpaRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByName(String hashtag);

}
