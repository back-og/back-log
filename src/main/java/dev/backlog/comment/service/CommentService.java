package dev.backlog.comment.service;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.dto.CommentCreateRequest;
import dev.backlog.comment.dto.CommentResponse;
import dev.backlog.comment.dto.CommentUpdateRequest;
import dev.backlog.common.exception.MissMatchException;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.backlog.comment.exception.CommentErrorCode.INVALID_WRITER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private static final String INVALID_WRITER_MESSAGE = "아이디(%s) 댓글의 작성자가 사용자 아이디(%s)와 일치하지 않습니다.";

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
            throw new MissMatchException(
                    INVALID_WRITER,
                    String.format(INVALID_WRITER_MESSAGE, commentId, userId)
            );
        }
    }

}
