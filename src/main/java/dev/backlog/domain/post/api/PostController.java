package dev.backlog.domain.post.api;


import dev.backlog.domain.post.dto.PostCreateRequest;
import dev.backlog.domain.post.dto.PostResponse;
import dev.backlog.domain.post.dto.PostSliceResponse;
import dev.backlog.domain.post.dto.PostSummaryResponse;
import dev.backlog.domain.post.dto.PostUpdateRequest;
import dev.backlog.domain.post.service.PostService;
import dev.backlog.domain.post.service.query.PostQueryService;
import dev.backlog.domain.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostQueryService postQueryService;

    // TODO: 2023/09/06 userId에서 nickname으로 변경

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PostCreateRequest request, AuthInfo authInfo) {
        Long postId = postService.create(request, authInfo);
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    @GetMapping
    public ResponseEntity<PostSliceResponse<PostSummaryResponse>> findSeriesPosts(String series,
                                                                                  Long userId,
                                                                                  @PageableDefault(size = 30, sort = "createdAt") Pageable pageable) {
        PostSliceResponse<PostSummaryResponse> seriesPosts = postQueryService.findPostsByUserAndSeries(userId, series, pageable);
        return ResponseEntity.ok(seriesPosts);
    }

    @GetMapping("/like")
    public ResponseEntity<PostSliceResponse<PostSummaryResponse>> findLikedPosts(Long userId,
                                                                                 @PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable) {
        PostSliceResponse<PostSummaryResponse> likedPosts = postQueryService.findLikedPostsByUser(userId, pageable);
        return ResponseEntity.ok(likedPosts);
    }

    @GetMapping("/recent")
    public ResponseEntity<PostSliceResponse<PostSummaryResponse>> findRecentPosts(@PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable) {
        PostSliceResponse<PostSummaryResponse> recentPosts = postQueryService.findPostsInLatestOrder(pageable);
        return ResponseEntity.ok(recentPosts);
    }

    @GetMapping("/trend")
    public ResponseEntity<PostSliceResponse<PostSummaryResponse>> findTrendPosts(@RequestParam(defaultValue = "week") String timePeriod,
                                                                                 Pageable pageable) {
        PostSliceResponse<PostSummaryResponse> trendPosts = postQueryService.findLikedPosts(timePeriod, pageable);
        return ResponseEntity.ok(trendPosts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long postId, Long userId) {
        PostResponse postResponse = postQueryService.findPostById(postId, userId);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<PostSliceResponse<PostSummaryResponse>> searchByNicknameAndHashtag(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String hashtag,
            @PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        PostSliceResponse<PostSummaryResponse> postSliceResponse = postQueryService.searchByUserNickname(nickname, hashtag, pageable);
        return ResponseEntity.ok(postSliceResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody PostUpdateRequest request,
                                           Long userId) {
        postService.updatePost(request, postId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

}
