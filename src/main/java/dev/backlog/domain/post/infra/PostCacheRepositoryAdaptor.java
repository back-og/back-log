package dev.backlog.domain.post.infra;

import dev.backlog.domain.post.infra.redis.PostRedisRepository;
import dev.backlog.domain.post.model.UserViewInfo;
import dev.backlog.domain.post.model.repository.PostCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCacheRepositoryAdaptor implements PostCacheRepository {

    private final PostRedisRepository postRedisRepository;

    @Override
    public Boolean existsByPostIdAndUserId(Long postId, Long userId) {
        return postRedisRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    public UserViewInfo save(UserViewInfo userViewInfo) {
        return postRedisRepository.save(userViewInfo);
    }

}
