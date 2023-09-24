package dev.backlog.common;

import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HTMLCharacterEscapesTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("응답 데이터에 html escape 처리를 할 수 있다.")
    @Test
    void htmlEscapeTest() {
        //given
        String actualContent = "<h1>내용<h1>";
        String responseContent = "&lt;h1&gt;내용&lt;h1&gt;";

        User user = userRepository.save(유저());
        Post post = postRepository.save(Post.builder()
                .series(null)
                .user(user)
                .title("제목")
                .content(actualContent)
                .summary("요약")
                .isPublic(true)
                .thumbnailImage("썸네일URL")
                .path("경로")
                .build());

        //when
        ResponseEntity<PostResponse> response = restTemplate.getForEntity("/api/posts/v1/" + post.getId(), PostResponse.class);

        //then
        assertThat(response.getBody().content()).isEqualTo(responseContent);
    }

}
