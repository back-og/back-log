package dev.backlog.like.service;

import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.like.dto.LikeStatusResponse;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PostLikeServiceTest {

    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeService postLikeService;

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("게시물에 좋아요를 최초로 누르면 좋아요가 상승한다.")
    @Test
    void switchLikeTest() {
        User user = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user, null));

        AuthInfo authInfo = new AuthInfo(post.getId(), "토큰");
        LikeStatusResponse likeStatusResponse = postLikeService.switchLike(post.getId(), authInfo);

        assertAll(
                () -> assertThat(likeStatusResponse.likeCount()).isOne(),
                () -> assertThat(likeStatusResponse.like()).isTrue()
        );
    }

    @DisplayName("게시물에 좋아요를 누른상태에서 한번 더 누르면 취소된다.")
    @Test
    void doubleSwitchLikeTest() {
        User user = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user, null));
        postLikeRepository.save(new PostLike(user, post));

        AuthInfo authInfo = new AuthInfo(post.getId(), "토큰");
        LikeStatusResponse likeStatusResponse = postLikeService.switchLike(post.getId(), authInfo);

        assertAll(
                () -> assertThat(likeStatusResponse.likeCount()).isZero(),
                () -> assertThat(likeStatusResponse.like()).isFalse()
        );
    }

}
