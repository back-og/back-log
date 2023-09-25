package dev.backlog.post.service.query;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.common.exception.InvalidAuthException;
import dev.backlog.post.domain.Post;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.backlog.auth.exception.AuthErrorCode.AUTHORIZATION_FAILED;

@Service
@RequiredArgsConstructor
public class PrivatePostAccessStrategy implements PostAccessStrategy {

    private static final String POST_AUTHORIZATION_FAILED_MESSAGE = "ID(%s)의 사용자 정보는 ID(%s)의 게시글에 대한 접근 권한이 없습니다.";

    private final CommentRepository commentRepository;

    @Override
    public PostResponse findPostById(Post post, AuthInfo authInfo) {
        List<Comment> comments = commentRepository.findAllByPost(post);
        if (authInfo == null || !authInfo.userId().equals(post.getUser().getId())) {
            throw new InvalidAuthException(
                    AUTHORIZATION_FAILED,
                    String.format(POST_AUTHORIZATION_FAILED_MESSAGE, authInfo.userId(), post.getId())
            );
        }
        return PostResponse.from(post, comments);
    }

}
