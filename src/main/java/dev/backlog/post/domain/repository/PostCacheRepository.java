package dev.backlog.post.domain.repository;

import dev.backlog.post.domain.UserViewInfo;

public interface PostCacheRepository {

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

    UserViewInfo save(UserViewInfo userViewInfo);

    void deleteAll();

}
