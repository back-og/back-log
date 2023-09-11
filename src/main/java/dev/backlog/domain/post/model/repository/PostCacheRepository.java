package dev.backlog.domain.post.model.repository;

import dev.backlog.domain.post.model.UserViewInfo;

public interface PostCacheRepository {

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

    UserViewInfo save(UserViewInfo userViewInfo);

}
