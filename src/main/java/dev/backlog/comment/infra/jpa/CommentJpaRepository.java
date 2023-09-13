package dev.backlog.comment.infra.jpa;

import dev.backlog.comment.domain.Comment;
import dev.backlog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    int countByPost(Post post);

}
