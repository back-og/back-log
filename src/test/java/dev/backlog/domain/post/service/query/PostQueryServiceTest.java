package dev.backlog.domain.post.service.query;

import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.domain.comment.infra.jpa.CommentJpaRepository;
import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.hashtag.infrastructure.persistence.HashtagJpaRepository;
import dev.backlog.domain.like.infrastructure.persistence.LikeJpaRepository;
import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.post.dto.PostResponse;
import dev.backlog.domain.post.dto.PostSliceResponse;
import dev.backlog.domain.post.dto.PostSummaryResponse;
import dev.backlog.domain.post.infra.jpa.PostJpaRepository;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.repository.PostHashtagRepository;
import dev.backlog.domain.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.dto.AuthInfo;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.게시물_모음;
import static dev.backlog.common.fixture.EntityFixture.댓글_모음;
import static dev.backlog.common.fixture.EntityFixture.시리즈1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static dev.backlog.common.fixture.EntityFixture.좋아요1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PostQueryServiceTest extends TestContainerConfig {

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PostJpaRepository postRepository;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private SeriesJpaRepository seriesJpaRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private HashtagJpaRepository hashtagJpaRepository;

    private User 유저1;
    private Post 게시물1;
    private List<Post> 게시물_모음;
    private List<Comment> 댓글_모음;

    @BeforeEach
    void setUp() {
        유저1 = 유저1();
        게시물1 = 게시물1(유저1, null);
        게시물_모음 = 게시물_모음(유저1, null);
        댓글_모음 = 댓글_모음(유저1, 게시물1);
    }

    @AfterEach
    void tearDown() {
        likeJpaRepository.deleteAll();
        commentJpaRepository.deleteAll();
        postHashtagRepository.deleteAll();
        hashtagJpaRepository.deleteAll();
        postRepository.deleteAll();
        seriesJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @DisplayName("게시글을 상세 조회할 수 있다.")
    @Test
    void findPostByIdTest() {
        //given
        User user = userJpaRepository.save(유저1);
        AuthInfo authInfo = new AuthInfo(user.getId());
        Post post = postRepository.save(게시물1);
        commentJpaRepository.saveAll(댓글_모음);

        //when
        PostResponse postResponse = postQueryService.findPostById(post.getId(), authInfo);

        //then
        assertThat(postResponse.postId()).isEqualTo(post.getId());
    }

    @DisplayName("같은 유저는 동일한 게시글의 조회수를 3시간동안 올릴 수 없다.")
    @Test
    void sameUserCannotIncreaseViewCountForSamePostWithin3Hours() {
        //given
        User user = userJpaRepository.save(유저1);
        AuthInfo authInfo = new AuthInfo(user.getId());
        Post post = postRepository.save(게시물1);
        commentJpaRepository.saveAll(댓글_모음);

        //when
        PostResponse firstSamePostResponse = postQueryService.findPostById(post.getId(), authInfo);
        PostResponse secondSamePostResponse = postQueryService.findPostById(post.getId(), authInfo);

        //then
        long increasedViewCount = 1L;

        assertAll(
                () -> assertThat(secondSamePostResponse.viewCount()).isEqualTo(increasedViewCount),
                () -> assertThat(firstSamePostResponse.viewCount()).isEqualTo(secondSamePostResponse.viewCount())
        );
    }

    @DisplayName("사용자가 좋아요를 누른 글들을 최신 순으로 조회할 수 있다.")
    @Test
    void findLikedPostsByUserTest() {
        //given
        User user = userJpaRepository.save(유저1);
        AuthInfo authInfo = new AuthInfo(user.getId());

        List<Post> posts = postRepository.saveAll(게시물_모음);
        for (Post post : posts) {
            Like like = 좋아요1(user, post);
            likeJpaRepository.save(like);
        }

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");

        //when
        PostSliceResponse<PostSummaryResponse> postSliceResponse = postQueryService.findLikedPostsByUser(authInfo, pageRequest);

        //then
        assertAll(
                () -> assertThat(postSliceResponse.data()).isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::createdAt).reversed()),
                () -> assertThat(postSliceResponse.hasNext()).isFalse(),
                () -> assertThat(postSliceResponse.numberOfElements()).isEqualTo(postSliceResponse.data().size())
        );
    }

    @DisplayName("사용자 닉네임과 시리즈 이름으로 게시글들을 과거순으로 조회할 수 있다.")
    @Test
    void findPostsByUserAndSeriesTest() {
        //given
        User user = userJpaRepository.save(유저1);
        Series series = seriesJpaRepository.save(시리즈1(user));
        postRepository.saveAll(게시물_모음(user, series));

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.ASC, "createdAt");

        //when
        PostSliceResponse<PostSummaryResponse> postSliceResponse = postQueryService.findPostsByUserAndSeries(user.getNickname(), series.getName(), pageRequest);

        //then
        assertAll(
                () -> assertThat(postSliceResponse.data()).isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::createdAt)),
                () -> assertThat(postSliceResponse.hasNext()).isFalse(),
                () -> assertThat(postSliceResponse.numberOfElements()).isEqualTo(postSliceResponse.data().size())
        );
    }

    @DisplayName("최신 순서로 등록된 게시물 목록을 조회할 수 있다.")
    @Test
    void findPostsInLatestOrderTest() {
        //given
        userJpaRepository.save(유저1);
        postRepository.saveAll(게시물_모음);

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");

        //when
        PostSliceResponse<PostSummaryResponse> postSliceResponse = postQueryService.findPostsInLatestOrder(pageRequest);

        //then
        assertAll(
                () -> assertThat(postSliceResponse.data()).isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::createdAt).reversed()),
                () -> assertThat(postSliceResponse.hasNext()).isFalse(),
                () -> assertThat(postSliceResponse.numberOfElements()).isEqualTo(postSliceResponse.data().size())
        );
    }

    @DisplayName("좋아요 많이 받은 순서로 게시물을 조회할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"today, week, month, year, default"})
    void findLikedPostsTest(String timePeriod) {
        //given
        User user1 = 유저1();
        User user2 = 유저1();
        userJpaRepository.saveAll(List.of(user1, user2));

        Post post1 = 게시물1(user1, null);
        Post post2 = 게시물1(user1, null);
        postRepository.saveAll(List.of(post1, post2));

        Like like1 = 좋아요1(user1, post1);
        Like like2 = 좋아요1(user1, post2);
        Like like3 = 좋아요1(user2, post1);
        likeJpaRepository.saveAll(List.of(like1, like2, like3));

        PageRequest pageRequest = PageRequest.of(0, 30);

        //when
        PostSliceResponse<PostSummaryResponse> postSliceResponse = postQueryService.findLikedPosts(timePeriod, pageRequest);

        //then
        assertAll(
                () -> assertThat(postSliceResponse.hasNext()).isFalse(),
                () -> assertThat(postSliceResponse.numberOfElements()).isEqualTo(postSliceResponse.data().size()),
                () -> assertThat(postSliceResponse.data().get(0).postId()).isEqualTo(post1.getId()),
                () -> assertThat(postSliceResponse.data().get(1).postId()).isEqualTo(post2.getId())
        );
    }

    @DisplayName("사용자 닉네임으로 게시물리스트를 조회해 요약 리스트로 반환한다.")
    @Test
    void searchByUserNicknameTest() {
        User user = userJpaRepository.save(유저1);
        postRepository.saveAll(게시물_모음);

        int pageSize = 20;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.Direction.ASC, "createdAt");
        PostSliceResponse<PostSummaryResponse> postSliceResponse = postQueryService.searchByUserNickname(user.getNickname(), "", pageRequest);

        assertThat(postSliceResponse.numberOfElements()).isEqualTo(pageSize);
        assertThat(postSliceResponse.hasNext()).isTrue();
    }

}
