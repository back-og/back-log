package dev.backlog.post.service.query;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivatePostAccessStrategy implements PostAccessStrategy {

    private final CommentRepository commentRepository;

    @Override
    public PostResponse findPostById(Post post, AuthInfo authInfo) {
        List<Comment> comments = commentRepository.findAllByPost(post);
        if (authInfo == null || !authInfo.userId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("게시글에 접근 권한이 없습니다.");
        }
        return PostResponse.from(post, comments);
    }

}
