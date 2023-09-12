package dev.backlog.post.infra;

import dev.backlog.post.domain.UserViewInfo;
import dev.backlog.post.domain.repository.PostCacheRepository;
import dev.backlog.post.infra.redis.PostRedisRepository;
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
