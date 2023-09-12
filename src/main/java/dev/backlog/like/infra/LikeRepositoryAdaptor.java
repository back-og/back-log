package dev.backlog.like.infra;

import dev.backlog.like.domain.Like;
import dev.backlog.like.domain.repository.LikeRepository;
import dev.backlog.like.infra.jpa.LikeJpaRepository;
import dev.backlog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryAdaptor implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(like);
    }

    @Override
    public List<Like> saveAll(Iterable<Like> likes) {
        return likeJpaRepository.saveAll(likes);
    }

    @Override
    public List<Like> findAll() {
        return likeJpaRepository.findAll();
    }

    @Override
    public int countByPost(Post post) {
        return likeJpaRepository.countByPost(post);
    }

    @Override
    public void deleteAll() {
        likeJpaRepository.deleteAll();
    }

}
