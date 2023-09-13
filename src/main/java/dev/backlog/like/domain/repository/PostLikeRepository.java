package dev.backlog.like.domain.repository;

import dev.backlog.like.domain.PostLike;
import dev.backlog.post.domain.Post;
import dev.backlog.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {

    PostLike save(PostLike postLike);

    List<PostLike> saveAll(Iterable<PostLike> likes);

    List<PostLike> findAll();

    int countByPost(Post post);

    void delete(PostLike postLike);

    void deleteAll();

    Optional<PostLike> findByUserAndPostId(User user, Long postId);

}
