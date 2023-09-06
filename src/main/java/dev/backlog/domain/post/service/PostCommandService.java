package dev.backlog.domain.post.service;

import dev.backlog.domain.post.dto.PostCreateRequest;
import dev.backlog.domain.post.dto.PostUpdateRequest;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.repository.PostRepository;
import dev.backlog.domain.series.infrastructure.persistence.SeriesRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService {

    private final PostHashtagService postHashtagService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

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

    public void deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Post post = postRepository.getById(postId);

        post.verifyPostOwner(user);
        postRepository.delete(post);
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
