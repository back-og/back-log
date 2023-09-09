package dev.backlog.domain.post.service;

import dev.backlog.domain.post.dto.PostCreateRequest;
import dev.backlog.domain.post.dto.PostUpdateRequest;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.repository.PostRepository;
import dev.backlog.domain.series.infrastructure.persistence.SeriesJpaRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostHashtagService postHashtagService;
    private final PostRepository postRepository;
    private final UserJpaRepository userJpaRepository;
    private final SeriesJpaRepository seriesJpaRepository;

    public Long create(PostCreateRequest request, AuthInfo authInfo) {
        User user = findUserById(authInfo.userId());
        Series series = findSeriesByUserAndName(user, request.series());
        Post post = request.toEntity(series, user);

        postHashtagService.associatePostWithHashtags(request.hashtags(), post);

        return postRepository.save(post).getId();
    }

    public void updatePost(PostUpdateRequest request, Long postId, Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Post post = postRepository.getById(postId);
        updatePostByRequest(post, request);
        Series series = seriesJpaRepository.findByUserAndName(user, request.series())
                .orElse(null);

        postHashtagService.associatePostWithHashtags(request.hashtags(), post);
        post.updateSeries(series);
    }

    public void deletePost(Long postId, Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Post post = postRepository.getById(postId);

        post.verifyPostOwner(user);
        postRepository.delete(post);
    }

    private User findUserById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    private Series findSeriesByUserAndName(User user, String seriesName) {
        return seriesJpaRepository.findByUserAndName(user, seriesName)
                .orElse(null);
    }

    private void updatePostByRequest(Post post, PostUpdateRequest request) {
        post.updateTitle(request.title());
        post.updateContent(request.content());
        post.updateSummary(request.summary());
        post.updateIsPublic(request.isPublic());
        post.updateThumbnailImage(request.thumbnailImage());
        post.updatePath(request.path());
    }

}
