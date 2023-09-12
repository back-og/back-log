package dev.backlog.post.infra;

import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.repository.HashtagRepository;
import dev.backlog.post.infra.jpa.HashtagJpaRepository;
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
