package dev.backlog.domain.post.infra;

import dev.backlog.domain.hashtag.model.Hashtag;
import dev.backlog.domain.post.infra.jpa.PostHashtagJpaRepository;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.PostHashtag;
import dev.backlog.domain.post.model.repository.PostHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostHashtagRepositoryAdaptor implements PostHashtagRepository {

    private final PostHashtagJpaRepository postHashtagJpaRepository;

    @Override
    public List<PostHashtag> saveAll(Iterable<PostHashtag> postHashtags) {
        return postHashtagJpaRepository.saveAll(postHashtags);
    }

    @Override
    public List<PostHashtag> findByPost(Post post) {
        return postHashtagJpaRepository.findByPost(post);
    }

    @Override
    public void deleteAllByPost(Post post) {
        postHashtagJpaRepository.deleteAllByPost(post);
    }

    @Override
    public void deleteAll() {
        postHashtagJpaRepository.deleteAll();
    }

    @Override
    public boolean existsByHashtag(Hashtag hashtag) {
        return postHashtagJpaRepository.existsByHashtag(hashtag);
    }

}
