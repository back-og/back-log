package dev.backlog.post.service;

import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostCreateRequest;
import dev.backlog.post.dto.PostUpdateRequest;
import dev.backlog.series.domain.Series;
import dev.backlog.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostHashtagService postHashtagService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SeriesJpaRepository seriesJpaRepository;

    public Long create(PostCreateRequest request, AuthInfo authInfo) {
        User user = userRepository.getById(authInfo.userId());
        Series series = findSeriesByUserAndName(user, request.series());
        Post post = request.toEntity(series, user);

        postHashtagService.associatePostWithHashtags(request.hashtags(), post);

        return postRepository.save(post).getId();
    }

    public void updatePost(PostUpdateRequest request, Long postId, AuthInfo authInfo) {
        User user = userRepository.getById(authInfo.userId());
        Post post = postRepository.getById(postId);
        Series series = seriesJpaRepository.findByUserAndName(user, request.series())
                .orElse(null);

        updatePostByRequest(post, request);
        post.updateSeries(series);
        postHashtagService.associatePostWithHashtags(request.hashtags(), post);
    }

    public void deletePost(Long postId, AuthInfo authInfo) {
        User user = userRepository.getById(authInfo.userId());
        Post post = postRepository.getById(postId);

        post.verifyPostOwner(user);
        postRepository.delete(post);
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
