package dev.backlog.domain.post.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "viewHistory", timeToLive = 10800)
public class ViewHistory {

    @Id
    private Long id;
    @Indexed
    private Long postId;
    @Indexed
    private Long userId;

    public ViewHistory(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

}
