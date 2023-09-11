package dev.backlog.domain.like.infra;

import dev.backlog.domain.like.infra.jpa.LikeJpaRepository;
import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.like.model.repository.LikeRepository;
import dev.backlog.domain.post.model.Post;
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
