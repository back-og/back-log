package dev.backlog.domain.hashtag.infrastructure;

import dev.backlog.domain.hashtag.infrastructure.jpa.HashtagJpaRepository;
import dev.backlog.domain.hashtag.model.Hashtag;
import dev.backlog.domain.hashtag.model.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HashtagRepositoryAdapter implements HashtagRepository {

    private final HashtagJpaRepository hashtagJpaRepository;

    @Override
    public Hashtag save(Hashtag hashtag) {
        return hashtagJpaRepository.save(hashtag);
    }

    @Override
    public Optional<Hashtag> findByName(String hashtag) {
        return hashtagJpaRepository.findByName(hashtag);
    }

    @Override
    public List<Hashtag> saveAll(Iterable<Hashtag> hashtags) {
        return hashtagJpaRepository.saveAll(hashtags);
    }

    @Override
    public void deleteAll() {
        hashtagJpaRepository.deleteAll();
    }

}
