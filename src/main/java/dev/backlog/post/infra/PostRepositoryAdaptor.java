package dev.backlog.post.infra;

import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.infra.jpa.PostJpaRepository;
import dev.backlog.series.domain.Series;
import dev.backlog.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdaptor implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public List<Post> saveAll(Iterable<Post> posts) {
        return postJpaRepository.saveAll(posts);
    }

    @Override
    public Slice<Post> findAll(Pageable pageable) {
        return postJpaRepository.findAll(pageable);
    }

    @Override
    public Post getById(Long postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(post);
    }

    @Override
    public void deleteAll() {
        postJpaRepository.deleteAll();
    }

    @Override
    public Slice<Post> findLikedPostsByUserId(Long userId, Pageable pageable) {
        return postJpaRepository.findLikedPostsByUserId(userId, pageable);
    }

    @Override
    public Slice<Post> findAllByUserAndSeries(User user, Series series, Pageable pageable) {
        return postJpaRepository.findAllByUserAndSeries(user, series, pageable);
    }

    @Override
    public int countBySeries(Series series) {
        return postJpaRepository.countBySeries(series);
    }

    @Override
    public List<Post> findAllBySeriesOrderByCreatedAt(Series series) {
        return postJpaRepository.findAllBySeriesOrderByCreatedAt(series);
    }

}
