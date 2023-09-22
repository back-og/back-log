package dev.backlog.post.infra;

import dev.backlog.common.exception.NotFoundException;
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

import static dev.backlog.post.exception.PostErrorCode.POST_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdaptor implements PostRepository {

    private static final String POST_NOT_FOUND_MESSAGE = "입력된 게시글 아이디(%s)로 게시글을 찾을 수 없습니다.";

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
                .orElseThrow(() -> new NotFoundException(
                                POST_NOT_FOUND,
                                String.format(POST_NOT_FOUND_MESSAGE, postId)
                        )
                );
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
    public List<Post> findAllBySeriesOrderByCreatedAt(Series series) {
        return postJpaRepository.findAllBySeriesOrderByCreatedAt(series);
    }

}
