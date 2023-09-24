package dev.backlog.like.infra.jpa;

import dev.backlog.like.domain.PostLike;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {

    int countByPost(Post post);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<PostLike> findByUserAndPostId(User user, Long postId);

}
