package dev.backlog.domain.post.infra.jpa;

import dev.backlog.domain.hashtag.model.Hashtag;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagJpaRepository extends JpaRepository<PostHashtag, Long> {

    void deleteAllByPost(Post post);

    List<PostHashtag> findByPost(Post post);

    boolean existsByHashtag(Hashtag hashtag);

}