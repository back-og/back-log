package dev.backlog.post.infra.redis;

import dev.backlog.post.domain.UserViewInfo;
import org.springframework.data.repository.CrudRepository;

public interface PostRedisRepository extends CrudRepository<UserViewInfo, Long> {

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

}
