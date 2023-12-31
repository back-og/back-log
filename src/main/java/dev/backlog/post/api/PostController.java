package dev.backlog.post.api;


import dev.backlog.common.annotation.Login;
import dev.backlog.common.dto.SliceResponse;
import dev.backlog.post.dto.PostCreateRequest;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.post.dto.PostSummaryResponse;
import dev.backlog.post.dto.PostUpdateRequest;
import dev.backlog.post.dto.SeriesPostsFindRequest;
import dev.backlog.post.service.PostService;
import dev.backlog.post.service.query.PostQueryService;
import dev.backlog.user.dto.AuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/api/posts/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostQueryService postQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PostCreateRequest request, @Login AuthInfo authInfo) {
        Long postId = postService.create(request, authInfo);
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    @GetMapping("/series")
    public ResponseEntity<SliceResponse<PostSummaryResponse>> findSeriesPosts(@Valid @ModelAttribute SeriesPostsFindRequest seriesPostsFindRequest,
                                                                              @PageableDefault(size = 30, sort = "createdAt") Pageable pageable) {
        SliceResponse<PostSummaryResponse> seriesPosts = postQueryService.findPostsByUserAndSeries(seriesPostsFindRequest, pageable);
        return ResponseEntity.ok(seriesPosts);
    }

    @GetMapping("/like")
    public ResponseEntity<SliceResponse<PostSummaryResponse>> findLikedPosts(@Login AuthInfo authInfo,
                                                                             @PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable) {
        SliceResponse<PostSummaryResponse> likedPosts = postQueryService.findLikedPostsByUser(authInfo, pageable);
        return ResponseEntity.ok(likedPosts);
    }

    @GetMapping("/recent")
    public ResponseEntity<SliceResponse<PostSummaryResponse>> findRecentPosts(@PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable) {
        SliceResponse<PostSummaryResponse> recentPosts = postQueryService.findPostsInLatestOrder(pageable);
        return ResponseEntity.ok(recentPosts);
    }

    @GetMapping("/trend")
    public ResponseEntity<SliceResponse<PostSummaryResponse>> findTrendPosts(@RequestParam(defaultValue = "week") String timePeriod,
                                                                             Pageable pageable) {
        SliceResponse<PostSummaryResponse> trendPosts = postQueryService.findLikedPosts(timePeriod, pageable);
        return ResponseEntity.ok(trendPosts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long postId, @Login(required = false) AuthInfo authInfo) {
        PostResponse postResponse = postQueryService.findPostById(postId, authInfo);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<SliceResponse<PostSummaryResponse>> searchByNicknameAndHashtag(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String hashtag,
            @PageableDefault(size = 30, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        SliceResponse<PostSummaryResponse> sliceResponse = postQueryService.searchByNicknameAndHashtag(nickname, hashtag, pageable);
        return ResponseEntity.ok(sliceResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@Valid @RequestBody PostUpdateRequest request,
                                           @PathVariable Long postId,
                                           @Login AuthInfo authInfo) {
        postService.updatePost(request, postId, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @Login AuthInfo authInfo) {
        postService.deletePost(postId, authInfo);
        return ResponseEntity.noContent().build();
    }

}
