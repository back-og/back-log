package dev.backlog.domain.comment.infra.jpa;

import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    int countByPost(Post post);

}
