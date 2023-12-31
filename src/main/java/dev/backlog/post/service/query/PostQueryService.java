package dev.backlog.post.service.query;

import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.common.dto.SliceResponse;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostQueryRepository;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.post.dto.PostSummaryResponse;
import dev.backlog.post.dto.SeriesPostsFindRequest;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final Map<Boolean, PostAccessStrategy> postAccessStrategyMap;

    @Transactional
    public PostResponse findPostById(Long postId, AuthInfo authInfo) {
        Post post = postRepository.getById(postId);
        Boolean isPublic = post.getIsPublic();
        return postAccessStrategyMap.get(isPublic).findPostById(post, authInfo);
    }

    public SliceResponse<PostSummaryResponse> searchByNicknameAndHashtag(String nickname, String hashtag, Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = fetchPostsByUserNickname(nickname, hashtag, pageable);
        return SliceResponse.from(postSummaryResponses);
    }

    public SliceResponse<PostSummaryResponse> findLikedPostsByUser(AuthInfo authInfo, Pageable pageable) {
        User user = userRepository.getById(authInfo.userId());

        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findLikedPostsByUserId(user.getId(), pageable)
                .map(this::createPostSummaryResponse);
        return SliceResponse.from(postSummaryResponses);
    }

    public SliceResponse<PostSummaryResponse> findPostsByUserAndSeries(SeriesPostsFindRequest seriesPostsFindRequest, Pageable pageable) {
        User user = userRepository.getByNickname(seriesPostsFindRequest.nickname());
        Series series = seriesRepository.getByUserAndName(user, seriesPostsFindRequest.series());
        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findAllByUserAndSeries(user, series, pageable)
                .map(this::createPostSummaryResponse);
        return SliceResponse.from(postSummaryResponses);
    }

    public SliceResponse<PostSummaryResponse> findPostsInLatestOrder(Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = postRepository.findAll(pageable)
                .map(this::createPostSummaryResponse);
        return SliceResponse.from(postSummaryResponses);
    }

    public SliceResponse<PostSummaryResponse> findLikedPosts(String timePeriod, Pageable pageable) {
        Slice<PostSummaryResponse> postSummaryResponses = postQueryRepository.findLikedPostsByTimePeriod(timePeriod, pageable)
                .map(this::createPostSummaryResponse);
        return SliceResponse.from(postSummaryResponses);
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
        return commentRepository.countByPost(post);
    }

    private int countLikesByPost(Post post) {
        return postLikeRepository.countByPost(post);
    }

}
