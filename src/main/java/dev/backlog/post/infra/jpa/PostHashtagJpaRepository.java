package dev.backlog.post.infra.jpa;

import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagJpaRepository extends JpaRepository<PostHashtag, Long> {

    void deleteAllByPost(Post post);

    List<PostHashtag> findByPost(Post post);

    boolean existsByHashtag(Hashtag hashtag);

}
