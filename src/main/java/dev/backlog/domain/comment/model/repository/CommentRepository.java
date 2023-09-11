package dev.backlog.domain.comment.model.repository;

import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.post.model.Post;

import java.util.List;

public interface CommentRepository {

    List<Comment> saveAll(Iterable<Comment> comments);

    List<Comment> findAllByPost(Post post);

    int countByPost(Post post);

}
