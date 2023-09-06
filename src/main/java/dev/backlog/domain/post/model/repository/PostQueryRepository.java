package dev.backlog.domain.post.model.repository;

import dev.backlog.domain.post.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostQueryRepository {

    Slice<Post> findLikedPostsByTimePeriod(String timePeriod, Pageable pageable);

    Slice<Post> findByUserNicknameAndHashtag(String nickName, String hashtag, Pageable pageable);

}
