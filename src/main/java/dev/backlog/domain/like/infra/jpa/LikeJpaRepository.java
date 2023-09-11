package dev.backlog.domain.like.infra.jpa;

import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {

    int countByPost(Post post);

}
