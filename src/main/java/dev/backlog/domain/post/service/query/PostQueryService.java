package dev.backlog.domain.post.service.query;

import dev.backlog.domain.comment.infrastructure.persistence.CommentJpaRepository;
import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.like.infrastructure.persistence.LikeJpaRepository;
import dev.backlog.domain.post.dto.PostResponse;
import dev.backlog.domain.post.dto.PostSliceResponse;
import dev.backlog.domain.post.dto.PostSummaryResponse;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.ViewHistory;
import dev.backlog.domain.post.model.repository.PostCacheRepository;
import dev.backlog.domain.post.model.repository.PostQueryRepository;
import dev.backlog.domain.post.model.repository.PostRepository;
import dev.backlog.domain.series.infrastructure.persistence.SeriesJpaRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostCacheRepository postCacheRepository;
    private final UserJpaRepository userJpaRepository;
    private final SeriesJpaRepository seriesJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final LikeJpaRepository likeJpaRepository;

    @Transactional
    public PostResponse findPostById(Long postId, Long userId) {
        Post post = postRepository.getById(postId);
        List<Comment> comments = commentJpaRepository.findAllByPost(post);

        if (Boolean.FALSE.equals(postCacheRepository.existsByPostIdAndUserId(postId, userId))) {
            postCacheRepository.save(new ViewHistory(postId, userId));
            post.increaseViewCount();
        }
        return PostResponse.from(post, comments);
    }

    public PostSliceResponse<PostSummaryResponse> searchByUserNickname(String nickname, String hashtag, Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = fetchPostsByUserNickname(nickname, hashtag, pageable);
        return PostSliceResponse.from(postSummaryResponses);
    }

    public PostSliceResponse<PostSummaryResponse> findLikedPostsByUser(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findLikedPostsByUserId(user.getId(), pageable)
                .map(this::createPostSummaryResponse);
        return PostSliceResponse.from(postSummaryResponses);
    }

    public PostSliceResponse<PostSummaryResponse> findPostsByUserAndSeries(String nickname, String seriesName, Pageable pageable) {
        User user = userJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Series series = seriesJpaRepository.findByUserAndName(user, seriesName)
                .orElse(null);

        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findAllByUserAndSeries(user, series, pageable)
                .map(this::createPostSummaryResponse);
        return PostSliceResponse.from(postSummaryResponses);
    }

    public PostSliceResponse<PostSummaryResponse> findPostsInLatestOrder(Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findAll(pageable)
                .map(this::createPostSummaryResponse);
        return PostSliceResponse.from(postSummaryResponses);
    }

    public PostSliceResponse<PostSummaryResponse> findLikedPosts(String timePeriod, Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = postQueryRepository.findLikedPostsByTimePeriod(timePeriod, pageable)
                .map(this::createPostSummaryResponse);
        return PostSliceResponse.from(postSummaryResponses);
    }

    private Slice<PostSummaryResponse> fetchPostsByUserNickname(String nickname, String hashtag, Pageable pageable) {
        return postQueryRepository.findByUserNicknameAndHashtag(nickname, hashtag, pageable)
                .map(this::createPostSummaryResponse);
    }

    private PostSummaryResponse createPostSummaryResponse(Post post) {
        int commentCount = countCommentsByPost(post);
        int likeCount = countLikesByPost(post);
        return PostSummaryResponse.of(post, commentCount, likeCount);
    }

    private int countCommentsByPost(Post post) {
        return commentJpaRepository.countByPost(post);
    }

    private int countLikesByPost(Post post) {
        return likeJpaRepository.countByPost(post);
    }

}
