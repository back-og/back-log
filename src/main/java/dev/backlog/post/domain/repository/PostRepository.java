package dev.backlog.post.domain.repository;

import dev.backlog.post.domain.Post;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepository {

    Post save(Post post);

    List<Post> saveAll(Iterable<Post> posts);

    Slice<Post> findAll(Pageable pageable);

    Post getById(Long postId);

    void delete(Post post);

    void deleteAll();

    Slice<Post> findLikedPostsByUserId(Long userId, Pageable pageable);

    Slice<Post> findAllByUserAndSeries(User user, Series series, Pageable pageable);

    List<Post> findAllBySeriesOrderByCreatedAt(Series series);

}
