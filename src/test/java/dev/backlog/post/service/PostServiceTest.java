package dev.backlog.post.service;

import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.common.exception.NotFoundException;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.PostHashtag;
import dev.backlog.post.domain.repository.HashtagRepository;
import dev.backlog.post.domain.repository.PostHashtagRepository;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostCreateRequest;
import dev.backlog.post.dto.PostUpdateRequest;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static dev.backlog.common.fixture.DtoFixture.게시물_수정_요청;
import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PostServiceTest extends TestContainerConfig {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    private User 유저1;
    private Post 게시물1;

    @BeforeEach
    void setUp() {
        유저1 = 유저();
        게시물1 = 공개_게시물(유저1, null);
    }

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAll();
        commentRepository.deleteAll();
        postHashtagRepository.deleteAll();
        hashtagRepository.deleteAll();
        postRepository.deleteAll();
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("포스트 생성요청과 유저의 아이디를 받아 게시물을 저장할 수 있다.")
    @Test
    void createTest() {
        User user = userRepository.save(유저1);

        PostCreateRequest request = new PostCreateRequest(
                null,
                "제목",
                "내용",
                null,
                "요약",
                true,
                "url",
                "/path"
        );

        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");
        Long postId = postService.create(request, authInfo);

        assertThat(postId).isNotNull();
    }

    @DisplayName("게시물 업데이트의 대한 정보를 받아서 게시물을 업데이트한다.")
    @Test
    void updatePostTest() {
        User user = userRepository.save(유저1);
        Post post = postRepository.save(게시물1);
        PostUpdateRequest request = 게시물_수정_요청();
        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");

        postService.updatePost(request, post.getId(), authInfo);

        Post 변경된_게시물 = postRepository.getById(post.getId());
        List<PostHashtag> postHashtags = postHashtagRepository.findByPost(변경된_게시물);
        assertAll(
                () -> assertThat(변경된_게시물.getTitle()).isEqualTo("변경된 제목"),
                () -> assertThat(변경된_게시물.getContent()).isEqualTo("변경된 내용"),
                () -> assertThat(변경된_게시물.getSummary()).isEqualTo("변경된 요약"),
                () -> assertThat(변경된_게시물.getThumbnailImage()).isEqualTo("변경된 URL"),
                () -> assertThat(변경된_게시물.getIsPublic()).isFalse(),
                () -> assertThat(변경된_게시물.getPath()).isEqualTo("변경된 경로"),
                () -> assertThat(postHashtags.size()).isOne()
        );
    }

    @DisplayName("게시물 작성자는 게시물을 삭제할 수 있다.")
    @Test
    void deletePostTest() {
        User user = userRepository.save(유저1);
        Post post = postRepository.save(게시물1);
        Long postId = post.getId();
        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");
        postService.deletePost(postId, authInfo);
        assertThatThrownBy(() -> postRepository.getById(postId))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("게시물 작성자가 아니면 게시물을 삭제할 수 없다.")
    @Test
    void deletePostFailTest() {
        User user = userRepository.save(유저1);
        Post post = postRepository.save(게시물1);
        Long postId = post.getId();
        assertThatThrownBy(() -> postService.deletePost(postId, new AuthInfo(user.getId() + 1, "토")))
                .isInstanceOf(RuntimeException.class);
    }

}
