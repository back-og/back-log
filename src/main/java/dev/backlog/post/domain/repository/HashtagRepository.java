package dev.backlog.post.domain.repository;

import dev.backlog.post.domain.Hashtag;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {

    Hashtag save(Hashtag hashtag);

    Optional<Hashtag> findByName(String hashtag);

    List<Hashtag> saveAll(Iterable<Hashtag> entities);

    void deleteAll();

}
