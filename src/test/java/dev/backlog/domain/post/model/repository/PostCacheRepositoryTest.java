package dev.backlog.domain.post.model.repository;

import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.domain.post.model.ViewHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostCacheRepositoryTest extends TestContainerConfig {

    @Autowired
    private PostCacheRepository postCacheRepository;

    @DisplayName("조회 내역을 저장할 수 있다.")
    @Test
    void save() {
        //given
        final long postId = 1l;
        final long userId = 1l;
        ViewHistory viewHistory = new ViewHistory(postId, userId);

        //when
        ViewHistory savedViewHistory = postCacheRepository.save(viewHistory);

        //then
        assertThat(viewHistory).isEqualTo(savedViewHistory);
    }

    @DisplayName("게시글 번호와 유저 아이디로 조회 내역 여부를 판단할 수 있다.")
    @Test
    void existsByPostIdAndUserId() {
        //given
        final long postId = 1l;
        final long userId = 1l;
        postCacheRepository.save(new ViewHistory(postId, userId));

        //when
        Boolean exists = postCacheRepository.existsByPostIdAndUserId(postId, userId);

        //then
        assertThat(exists).isTrue();
    }

}
