package dev.backlog.comment.service;

import java.util.List;
import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.dto.CommentCreateRequest;
import dev.backlog.comment.dto.CommentResponse;
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
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long create(CommentCreateRequest request, AuthInfo authInfo, Long postId) {
        User findUser = userRepository.getById(authInfo.userId());
        Post findPost = postRepository.getById(postId);

        Comment comment = request.toEntity(findUser, findPost);
        if (request.parentId() != null) {
            Comment parentComment = commentRepository.getById(request.parentId());
            comment.updateParent(parentComment);
        }

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void update(CommentUpdateRequest request, AuthInfo authInfo, Long commentId) {
        validateWriter(authInfo.userId(), commentId);

        Comment comment = commentRepository.getById(commentId);
        comment.updateContent(request.content());
    }

    public List<CommentResponse> findChildComments(Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        List<Comment> comments = comment.getChildren();
        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void delete(AuthInfo authInfo, Long commentId) {
        validateWriter(authInfo.userId(), commentId);

        Comment comment = commentRepository.getById(commentId);
        commentRepository.delete(comment);
    }

    private void validateWriter(Long userId, Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        Long writerId = comment.getWriter().getId();
        if (!writerId.equals(userId)) {
            throw new IllegalArgumentException("해당 댓글의 작성자가 아닙니다.");
        }
    }

}
