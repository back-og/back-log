package dev.backlog.post.service.query;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.UserViewInfo;
import dev.backlog.post.domain.repository.PostCacheRepository;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicPostAccessStrategy implements PostAccessStrategy {

    private final PostCacheRepository postCacheRepository;
    private final CommentRepository commentRepository;

    @Override
    public PostResponse findPostById(Post post, AuthInfo authInfo) {
        List<Comment> comments = commentRepository.findAllByPost(post);
        if (authInfo == null || authInfo.userId().equals(post.getUser().getId())) {
            return PostResponse.from(post, comments);
        }
        increaseViewCount(post, authInfo);
        return PostResponse.from(post, comments);
    }

    private void increaseViewCount(Post post, AuthInfo authInfo) {
        if (Boolean.FALSE.equals(postCacheRepository.existsByPostIdAndUserId(post.getId(), authInfo.userId()))) {
            postCacheRepository.save(new UserViewInfo(post.getId(), authInfo.userId()));
            post.increaseViewCount();
        }
    }

}
