package dev.backlog.like.infra;

import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.like.infra.jpa.PostLikeJpaRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<PostLike> findByUserAndPostId(User user, Long postId) {
        return postLikeJpaRepository.findByUserAndPostId(user, postId);
    }

    @Override
    public int countByPost(Post post) {
        return postLikeJpaRepository.countByPost(post);
    }

    @Override
    public void delete(PostLike postLike) {
        postLikeJpaRepository.delete(postLike);
    }

    @Override
    public void deleteAll() {
        postLikeJpaRepository.deleteAll();
    }

}
