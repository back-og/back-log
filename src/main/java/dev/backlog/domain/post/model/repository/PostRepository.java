package dev.backlog.domain.post.model.repository;

import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepository {

    Post save(Post post);

    List<Post> saveAll(Iterable<Post> posts);

    Slice<Post> findAll(Pageable pageable);

    Post getById(Long postId);

    void delete(Post post);

    Slice<Post> findLikedPostsByUserId(Long userId, Pageable pageable);

    Slice<Post> findAllByUserAndSeries(User user, Series series, Pageable pageable);

}
