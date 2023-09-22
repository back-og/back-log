package dev.backlog.like.api;

import dev.backlog.like.dto.LikeStatusResponse;
import dev.backlog.like.service.PostLikeService;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PutMapping("/{postId}/like/{userId}")
    public ResponseEntity<LikeStatusResponse> switchLike(@PathVariable Long postId, @PathVariable Long userId) {
        LikeStatusResponse likeStatusResponse = postLikeService.switchLike(postId, new AuthInfo(userId, "토큰"));
        return ResponseEntity.ok(likeStatusResponse);
    }

}
