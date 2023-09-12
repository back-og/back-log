package dev.backlog.like.domain.repository;

import dev.backlog.like.domain.PostLike;
import dev.backlog.post.domain.Post;

import java.util.List;

public interface PostLikeRepository {

    PostLike save(PostLike postLike);

    List<PostLike> saveAll(Iterable<PostLike> likes);

    List<PostLike> findAll();

    int countByPost(Post post);

    void deleteAll();

}
