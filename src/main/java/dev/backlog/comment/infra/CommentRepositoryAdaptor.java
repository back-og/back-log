package dev.backlog.comment.infra;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.infra.jpa.CommentJpaRepository;
import dev.backlog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdaptor implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public List<Comment> saveAll(Iterable<Comment> comments) {
        return commentJpaRepository.saveAll(comments);
    }

    @Override
    public List<Comment> findAllByPost(Post post) {
        return commentJpaRepository.findAllByPost(post);
    }

    @Override
    public int countByPost(Post post) {
        return commentJpaRepository.countByPost(post);
    }

    @Override
    public void deleteAll() {
        commentJpaRepository.deleteAll();
    }

}
