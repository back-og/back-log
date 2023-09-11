package dev.backlog.domain.like.model.repository;

import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.post.model.Post;

import java.util.List;

public interface LikeRepository {

    Like save(Like like);

    List<Like> saveAll(Iterable<Like> likes);

    List<Like> findAll();

    int countByPost(Post post);

    void deleteAll();

}
