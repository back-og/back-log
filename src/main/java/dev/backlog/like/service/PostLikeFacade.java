package dev.backlog.like.service;

import dev.backlog.user.dto.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLikeFacade {

    private final PostLikeService postLikeService;

    public void switchLike(Long postId, AuthInfo authInfo) throws InterruptedException {
        while (true) {
            try {
                postLikeService.switchLike(postId, authInfo);
                break;
            } catch (Exception e) {
                log.info("예외 발생", e);
                Thread.sleep(50);
            }
        }

    }
}
