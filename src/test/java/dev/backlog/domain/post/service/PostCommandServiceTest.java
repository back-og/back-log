package dev.backlog.domain.post.service;

import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.domain.comment.infrastructure.persistence.CommentRepository;
import dev.backlog.domain.hashtag.infrastructure.persistence.HashtagRepository;
import dev.backlog.domain.like.infrastructure.persistence.LikeRepository;
import dev.backlog.domain.post.dto.PostCreateRequest;
import dev.backlog.domain.post.dto.PostUpdateRequest;
import dev.backlog.domain.post.infra.jpa.PostHashtagRepository;
import dev.backlog.domain.post.infra.jpa.PostJpaRepository;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.PostHashtag;
import dev.backlog.domain.series.infrastructure.persistence.SeriesRepository;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PostCommandServiceTest extends TestContainerConfig {

    @Autowired
    private PostCommandService postCommandService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostJpaRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

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
        유저1 = 유저1();
        게시물1 = 게시물1(유저1, null);
    }

    @AfterEach
    void tearDown() {
        likeRepository.deleteAll();
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

        AuthInfo authInfo = new AuthInfo(user.getId());
        Long postId = postCommandService.create(request, authInfo);

        assertThat(postId).isNotNull();
    }

    @DisplayName("게시물 업데이트의 대한 정보를 받아서 게시물을 업데이트한다.")
    @Test
    void updatePostTest() {
        userRepository.save(유저1);
        postRepository.save(게시물1);
        PostUpdateRequest request = getPostUpdateRequest();
        postCommandService.updatePost(request, 게시물1.getId(), 유저1.getId());

        Post 변경된_게시물 = postRepository.findById(게시물1.getId()).get();
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
        userRepository.save(유저1);
        postRepository.save(게시물1);
        Long postId = 게시물1.getId();
        postCommandService.deletePost(postId, 유저1.getId());
        boolean result = postRepository.findById(postId).isPresent();

        assertThat(result).isFalse();
    }

    @DisplayName("게시물 작성자가 아니면 게시물을 삭제할 수 없다.")
    @Test
    void deletePostFailTest() {
        userRepository.save(유저1);
        postRepository.save(게시물1);
        Long postId = 게시물1.getId();
        Long userId = 유저1.getId();
        Assertions.assertThatThrownBy(() -> postCommandService.deletePost(postId, userId + 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private PostUpdateRequest getPostUpdateRequest() {
        return new PostUpdateRequest(
                null,
                "변경된 제목",
                "변경된 내용",
                Set.of("변경된 해쉬태그"),
                "변경된 요약",
                false,
                "변경된 URL",
                "변경된 경로"
        );
    }

}
