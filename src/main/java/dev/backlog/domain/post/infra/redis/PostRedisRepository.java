package dev.backlog.domain.post.infra.redis;

import dev.backlog.domain.post.model.UserViewInfo;
import org.springframework.data.repository.CrudRepository;

public interface PostRedisRepository extends CrudRepository<UserViewInfo, Long> {

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

}
