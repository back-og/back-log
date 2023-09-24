package dev.backlog.post.service.query;

import dev.backlog.common.exception.InvalidAuthException;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static dev.backlog.common.fixture.EntityFixture.비공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PrivatePostAccessStrategyTest {

    @Autowired
    private PrivatePostAccessStrategy privatePostAccessStrategy;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User 유저1;
    private Post 게시물1;

    @BeforeEach
    void setUp() {
        유저1 = 유저();
        게시물1 = 비공개_게시물(유저1, null);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("작성자는 비공개 게시물을 조회할 수 있다.")
    @Test
    void userFindPrivatePostByIdTest() {
        //given
        User user = userRepository.save(유저1);
        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");
        Post post = postRepository.save(게시물1);

        //when
        PostResponse postResponse = privatePostAccessStrategy.findPostById(post, authInfo);

        //then
        assertThat(postResponse.postId()).isEqualTo(post.getId());
    }

    @DisplayName("작성자가 아닌 사용자는 다른 사용자의 비공개 게시물을 조회할 수 없다.")
    @Test
    void notFindPrivatePostByIdTest() {
        User user = userRepository.save(유저1);
        Post post = postRepository.save(게시물1);

        long randomId = new Random().nextLong();
        Long anotherUserId = (randomId == user.getId()) ? 0l : randomId;
        AuthInfo authInfo = new AuthInfo(anotherUserId, "토큰");

        //when, then
        assertThatThrownBy(() -> privatePostAccessStrategy.findPostById(post, authInfo))
                .isInstanceOf(InvalidAuthException.class);
    }

}
