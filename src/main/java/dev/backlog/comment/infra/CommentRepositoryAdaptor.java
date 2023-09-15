package dev.backlog.comment.infra;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.infra.jpa.CommentJpaRepository;
import dev.backlog.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdaptor implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public List<Comment> saveAll(Iterable<Comment> comments) {
        return commentJpaRepository.saveAll(comments);
    }

    @Override
    public Comment getById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));
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

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

}
