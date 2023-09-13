package dev.backlog.like.infra.jpa;

import dev.backlog.like.domain.PostLike;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {

    int countByPost(Post post);

    public Optional<PostLike> findByUserAndPostId(User user, Long postId);

}
