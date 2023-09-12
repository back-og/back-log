package dev.backlog.post.domain.repository;

import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.PostHashtag;

import java.util.List;

public interface PostHashtagRepository {

    List<PostHashtag> saveAll(Iterable<PostHashtag> postHashtags);

    List<PostHashtag> findByPost(Post post);

    void deleteAllByPost(Post post);

    void deleteAll();

    boolean existsByHashtag(Hashtag hashtag);

}
