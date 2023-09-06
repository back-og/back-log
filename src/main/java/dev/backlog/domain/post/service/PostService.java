package dev.backlog.domain.post.service;

import dev.backlog.domain.comment.infrastructure.persistence.CommentRepository;
import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.like.infrastructure.persistence.LikeRepository;
import dev.backlog.domain.post.dto.PostCreateRequest;
import dev.backlog.domain.post.dto.PostResponse;
import dev.backlog.domain.post.dto.PostSliceResponse;
import dev.backlog.domain.post.dto.PostSummaryResponse;
import dev.backlog.domain.post.dto.PostUpdateRequest;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.repository.PostQueryRepository;
import dev.backlog.domain.post.model.repository.PostRepository;
import dev.backlog.domain.series.infrastructure.persistence.SeriesRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private static final String REDIS_KEY_PREFIX = "user:%s:post:%s";

    private final PostHashtagService postHashtagService;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final CommentRepository commentRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public PostResponse findPostById(Long postId, Long userId) {
        Post post = postRepository.getById(postId);
        List<Comment> comments = commentRepository.findAllByPost(post);

        String userAndPostRedisKey = String.format(REDIS_KEY_PREFIX, userId, postId);
        if (Boolean.FALSE.equals(redisTemplate.hasKey(userAndPostRedisKey))) {
            Long currentViewCount = post.getViewCount();
            Long increasedViewCount = currentViewCount + 1;
            redisTemplate.opsForValue().set(userAndPostRedisKey, String.valueOf(increasedViewCount));
            redisTemplate.expire(userAndPostRedisKey, Duration.ofHours(3));
            post.updateViewCount(increasedViewCount);
        }
        return PostResponse.from(post, comments);
    }

    @Transactional
    public Long create(PostCreateRequest request, AuthInfo authInfo) {
        User user = userRepository.findById(authInfo.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Series series = seriesRepository.findByUserAndName(user, request.series())
                .orElse(null);
        Post post = request.toEntity(series, user);

        Post savedPost = postRepository.save(post);
        if (request.hashtags() != null) {
            postHashtagService.save(request.hashtags(), post);
        }
        return savedPost.getId();
    }

    public PostSliceResponse<PostSummaryResponse> searchByUserNickname(String nickname, String hashtag, Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = fetchPostsByUserNickname(nickname, hashtag, pageable);
        return PostSliceResponse.from(postSummaryResponses);
    }

    public PostSliceResponse<PostSummaryResponse> findLikedPostsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findLikedPostsByUserId(user.getId(), pageable)
                .map(this::createPostSummaryResponse);
        return PostSliceResponse.from(postSummaryResponses);
    }

    public PostSliceResponse<PostSummaryResponse> findPostsByUserAndSeries(Long userId, String seriesName, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Series series = seriesRepository.findByUserAndName(user, seriesName)
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

    @Transactional
    public void updatePost(PostUpdateRequest request, Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Post post = postRepository.getById(postId);
        updatePostByRequest(post, request);
        Series series = seriesRepository.findByUserAndName(user, request.series())
                .orElse(null);
        post.updateSeries(series);
        postHashtagService.deleteAllByPost(post);
        if (request.hashtags() != null) {
            postHashtagService.save(request.hashtags(), post);
        }
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Post post = postRepository.getById(postId);

        post.verifyPostOwner(user);
        postRepository.delete(post);
    }

    private Slice<PostSummaryResponse> fetchPostsByUserNickname(String nickname, String hashtag, Pageable pageable) {
        return postQueryRepository.findByUserNicknameAndHashtag(nickname, hashtag, pageable)
                .map(this::createPostSummaryResponse);
    }

    private void updatePostByRequest(Post post, PostUpdateRequest request) {
        post.updateTitle(request.title());
        post.updateContent(request.content());
        post.updateSummary(request.summary());
        post.updateIsPublic(request.isPublic());
        post.updateThumbnailImage(request.thumbnailImage());
        post.updatePath(request.path());
    }

    private PostSummaryResponse createPostSummaryResponse(Post post) {
        int commentCount = countCommentsByPost(post);
        int likeCount = countLikesByPost(post);
        return PostSummaryResponse.of(post, commentCount, likeCount);
    }

    private int countCommentsByPost(Post post) {
        return commentRepository.countByPost(post);
    }

    private int countLikesByPost(Post post) {
        return likeRepository.countByPost(post);
    }

}
