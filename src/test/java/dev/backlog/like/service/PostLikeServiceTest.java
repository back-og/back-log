package dev.backlog.like.service;

import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @DisplayName("낙관적 락 동시성 문제 해결 실패 테스트")
    @Test
    void optimisticLockTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicLong userIdCounter = new AtomicLong(1);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    Long userId = userIdCounter.getAndIncrement();
                    postLikeService.toggleLikeStatus(1L, new AuthInfo(userId, "토큰"));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}
