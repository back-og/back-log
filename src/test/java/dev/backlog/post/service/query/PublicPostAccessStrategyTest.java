package dev.backlog.post.service.query;

import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostCacheRepository;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PublicPostAccessStrategyTest extends TestContainerConfig {

    @Autowired
    private PublicPostAccessStrategy publicPostAccessStrategy;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCacheRepository postCacheRepository;

    private User 유저;
    private Post 게시물;

    @BeforeEach
    void setUp() {
        유저 = 유저();
        게시물 = 공개_게시물(유저, null);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        postCacheRepository.deleteAll();
    }

    @DisplayName("회원은 공개 게시물을 조회할 수 있다.")
    @Test
    void userFindPublicPostByIdTest() {
        //given
        User user = userRepository.save(유저);
        Post post = postRepository.save(게시물);

        long randomId = new Random().nextLong();
        Long anotherUserId = (randomId == user.getId()) ? 0l : randomId;
        AuthInfo authInfo = new AuthInfo(anotherUserId, "토큰");

        //when
        PostResponse postResponse = publicPostAccessStrategy.findPostById(post, authInfo);

        //then
        final long increasedViewCount = 1l;
        assertAll(
                () -> assertThat(postResponse.postId()).isEqualTo(post.getId()),
                () -> assertThat(postResponse.viewCount()).isEqualTo(increasedViewCount)
        );
    }

    @DisplayName("비회원은 공개 게시물을 조회할 수 있다.")
    @ParameterizedTest
    @NullSource
    void anonymousUserFindPublicPostByIdTest(AuthInfo authInfo) {
        //given
        userRepository.save(유저);
        Post post = postRepository.save(게시물);

        //when
        PostResponse postResponse = publicPostAccessStrategy.findPostById(post, authInfo);

        //then
        final long viewCount = 0l;
        assertAll(
                () -> assertThat(postResponse.postId()).isEqualTo(post.getId()),
                () -> assertThat(postResponse.viewCount()).isEqualTo(viewCount)
        );
    }

}
