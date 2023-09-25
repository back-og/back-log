package dev.backlog.post.service.query;

import dev.backlog.post.domain.Post;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.dto.AuthInfo;

public interface PostAccessStrategy {

    PostResponse findPostById(Post post, AuthInfo authInfo);

}
