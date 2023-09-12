package dev.backlog.like.infra.jpa;

import dev.backlog.like.domain.PostLike;
import dev.backlog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {

    int countByPost(Post post);

}
