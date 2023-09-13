package dev.backlog.post.infra.jpa;

import dev.backlog.post.domain.Post;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Query(""" 
            SELECT p
            FROM Post p
            INNER JOIN Like l ON l.post.id = p.id
            WHERE p.isPublic = true AND l.user.id = :userId
            """)
    Slice<Post> findLikedPostsByUserId(Long userId, Pageable pageable);

    Slice<Post> findAllByUserAndSeries(User user, Series series, Pageable pageable);

    List<Post> findAllBySeriesOrderByCreatedAt(Series series);

}
