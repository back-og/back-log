package dev.backlog.domain.post.model.repository;

import dev.backlog.domain.post.model.ViewHistory;

public interface PostCacheRepository {

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

    ViewHistory save(ViewHistory viewHistory);

}
