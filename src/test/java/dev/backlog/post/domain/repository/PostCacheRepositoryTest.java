package dev.backlog.post.domain.repository;

import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.post.domain.UserViewInfo;
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
    void saveTest() {
        //given
        final long postId = 1l;
        final long userId = 1l;
        UserViewInfo userViewInfo = new UserViewInfo(postId, userId);

        //when
        UserViewInfo savedUserViewInfo = postCacheRepository.save(userViewInfo);

        //then
        assertThat(userViewInfo).isEqualTo(savedUserViewInfo);
    }

    @DisplayName("게시글 번호와 유저 아이디로 조회 내역 여부를 판단할 수 있다.")
    @Test
    void existsByPostIdAndUserIdTest() {
        //given
        final long postId = 1l;
        final long userId = 1l;
        postCacheRepository.save(new UserViewInfo(postId, userId));

        //when
        Boolean exists = postCacheRepository.existsByPostIdAndUserId(postId, userId);

        //then
        assertThat(exists).isTrue();
    }

}
