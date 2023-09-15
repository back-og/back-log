package dev.backlog.like.api;

import dev.backlog.common.annotation.Login;
import dev.backlog.like.dto.LikeStatus;
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

    @PutMapping("/{postId}/like")
    public ResponseEntity<LikeStatus> switchLike(@PathVariable Long postId, @Login AuthInfo authInfo) {
        LikeStatus likeStatus = postLikeService.switchLike(postId, authInfo);
        return ResponseEntity.ok(likeStatus);
    }

}
