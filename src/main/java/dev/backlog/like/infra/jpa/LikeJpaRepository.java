package dev.backlog.like.infra.jpa;

import dev.backlog.like.domain.Like;
import dev.backlog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {

    int countByPost(Post post);

}
