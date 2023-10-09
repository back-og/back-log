package dev.backlog.like.service;

import dev.backlog.like.dto.LikeStatusResponse;
import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLikeFacade {

    private final PostLikeService postLikeService;

    public LikeStatusResponse switchLike(Long postId, AuthInfo authInfo) {
        while (true) {
            try {
                return postLikeService.toggleLikeStatus(postId, authInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
