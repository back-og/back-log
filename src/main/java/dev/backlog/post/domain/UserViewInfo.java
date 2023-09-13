package dev.backlog.post.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "userViewInfo", timeToLive = 10800)
public class UserViewInfo {

    @Id
    private Long id;
    @Indexed
    private Long postId;
    @Indexed
    private Long userId;

    public UserViewInfo(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

}
