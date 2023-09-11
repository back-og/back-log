package dev.backlog.domain.comment.infra;

import dev.backlog.domain.comment.infra.jpa.CommentJpaRepository;
import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.comment.model.repository.CommentRepository;
import dev.backlog.domain.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdaptor implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public void saveAll(Iterable<Comment> comments) {
        commentJpaRepository.saveAll(comments);
    }

    @Override
    public List<Comment> findAllByPost(Post post) {
        return commentJpaRepository.findAllByPost(post);
    }

    @Override
    public int countByPost(Post post) {
        return commentJpaRepository.countByPost(post);
    }

}
