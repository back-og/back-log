package dev.backlog.comment.domain.repository;

import dev.backlog.comment.domain.Comment;
import dev.backlog.post.domain.Post;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    List<Comment> saveAll(Iterable<Comment> comments);

    Comment getById(Long commentId);

    List<Comment> findAllByPost(Post post);

    int countByPost(Post post);

    void deleteAll();

    void delete(Comment comment);

}
