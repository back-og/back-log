package dev.backlog.like.domain.repository;

import dev.backlog.like.domain.Like;
import dev.backlog.post.domain.Post;

import java.util.List;

public interface LikeRepository {

    Like save(Like like);

    List<Like> saveAll(Iterable<Like> likes);

    List<Like> findAll();

    int countByPost(Post post);

    void deleteAll();

}
