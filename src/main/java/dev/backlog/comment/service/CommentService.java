package dev.backlog.comment.service;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.dto.CommentCreateRequest;
import dev.backlog.comment.dto.CommentUpdateRequest;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long create(CommentCreateRequest request, AuthInfo authInfo, Long postId) {
        User findUser = userRepository.getById(authInfo.userId());
        Post findPost = postRepository.getById(postId);
        Comment comment = request.toEntity(findUser, findPost);

        return commentRepository.save(comment).getId();
    }

    public void update(CommentUpdateRequest request, AuthInfo authInfo, Long commentId) {
        validateWriter(authInfo.userId(), commentId);

        Comment comment = commentRepository.getById(commentId);
        comment.updateContent(request.content());
    }

    private void validateWriter(Long userId, Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        Long writerId = comment.getWriter().getId();
        if (!writerId.equals(userId)) {
            throw new IllegalArgumentException("해당 댓글을 수정할 수 없습니다.");
        }
    }

}
