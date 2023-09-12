package dev.backlog.like.infra;

import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.like.infra.jpa.PostLikeJpaRepository;
import dev.backlog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryAdaptor implements PostLikeRepository {

    private final PostLikeJpaRepository postLikeJpaRepository;

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeJpaRepository.save(postLike);
    }

    @Override
    public List<PostLike> saveAll(Iterable<PostLike> likes) {
        return postLikeJpaRepository.saveAll(likes);
    }

    @Override
    public List<PostLike> findAll() {
        return postLikeJpaRepository.findAll();
    }

    @Override
    public int countByPost(Post post) {
        return postLikeJpaRepository.countByPost(post);
    }

    @Override
    public void deleteAll() {
        postLikeJpaRepository.deleteAll();
    }

}
