package dev.backlog.domain.hashtag.model.repository;

import dev.backlog.domain.hashtag.model.Hashtag;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {

    Hashtag save(Hashtag hashtag);

    Optional<Hashtag> findByName(String hashtag);

    List<Hashtag> saveAll(Iterable<Hashtag> entities);

    void deleteAll();

}
