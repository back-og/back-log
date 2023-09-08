package dev.backlog.domain.post.infra.redis;

import dev.backlog.domain.post.model.ViewHistory;
import org.springframework.data.repository.CrudRepository;

public interface PostRedisRepository extends CrudRepository<ViewHistory, Long> {

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

}
