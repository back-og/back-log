package dev.backlog.domain.post.model.repository;

import dev.backlog.domain.hashtag.model.Hashtag;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.PostHashtag;

import java.util.List;

public interface PostHashtagRepository {

    List<PostHashtag> saveAll(Iterable<PostHashtag> postHashtags);

    List<PostHashtag> findByPost(Post post);

    void deleteAllByPost(Post post);

    void deleteAll();

    boolean existsByHashtag(Hashtag hashtag);

}
