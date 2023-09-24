package dev.backlog.post.service.query;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.common.config.TestContainerConfig;
import dev.backlog.common.dto.SliceResponse;
import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.HashtagRepository;
import dev.backlog.post.domain.repository.PostCacheRepository;
import dev.backlog.post.domain.repository.PostHashtagRepository;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.dto.PostResponse;
import dev.backlog.post.dto.PostSummaryResponse;
import dev.backlog.post.dto.SeriesPostsFindRequest;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.dto.AuthInfo;
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
import java.util.Random;

import static dev.backlog.common.fixture.EntityFixture.게시물_모음;
import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.댓글_모음;
import static dev.backlog.common.fixture.EntityFixture.시리즈;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static dev.backlog.common.fixture.EntityFixture.좋아요;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PostQueryServiceTest extends TestContainerConfig {

    @Autowired
    private PostQueryService postQueryService;

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

    @Autowired
    private PostCacheRepository postCacheRepository;

    private User 유저;
    private Post 게시물;
    private List<Post> 게시물_모음;
    private List<Comment> 댓글_모음;

    @BeforeEach
    void setUp() {
        유저 = 유저();
        게시물 = 공개_게시물(유저, null);
        게시물_모음 = 게시물_모음(유저, null);
        댓글_모음 = 댓글_모음(유저, 게시물);
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
        postCacheRepository.deleteAll();
    }

    @DisplayName("게시글을 상세 조회할 수 있다.")
    @Test
    void findPostByIdTest() {
        //given
        User user = userRepository.save(유저);
        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");
        Post post = postRepository.save(게시물);
        commentRepository.saveAll(댓글_모음);

        //when
        PostResponse postResponse = postQueryService.findPostById(post.getId(), authInfo);

        //then
        assertThat(postResponse.postId()).isEqualTo(post.getId());
    }

    @DisplayName("같은 유저는 동일한 게시글의 조회수를 3시간동안 올릴 수 없다.")
    @Test
    void sameUserCannotIncreaseViewCountForSamePostWithin3Hours() {
        //given
        User user = userRepository.save(유저);
        Post post = postRepository.save(게시물);
        commentRepository.saveAll(댓글_모음);

        long randomId = new Random().nextLong();
        Long anotherUserId = (randomId == user.getId()) ? 0l : randomId;
        AuthInfo authInfo = new AuthInfo(anotherUserId, "토큰");

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
        User user = userRepository.save(유저);
        AuthInfo authInfo = new AuthInfo(user.getId(), "토큰");

        List<Post> posts = postRepository.saveAll(게시물_모음);
        for (Post post : posts) {
            PostLike postLike = 좋아요(user, post);
            postLikeRepository.save(postLike);
        }

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");

        //when
        SliceResponse<PostSummaryResponse> sliceResponse = postQueryService.findLikedPostsByUser(authInfo, pageRequest);

        //then
        assertAll(
                () -> assertThat(sliceResponse.data()).isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::createdAt).reversed()),
                () -> assertThat(sliceResponse.hasNext()).isFalse(),
                () -> assertThat(sliceResponse.numberOfElements()).isEqualTo(sliceResponse.data().size())
        );
    }

    @DisplayName("사용자 닉네임과 시리즈 이름으로 게시글들을 과거순으로 조회할 수 있다.")
    @Test
    void findPostsByUserAndSeriesTest() {
        //given
        User user = userRepository.save(유저);
        Series series = seriesRepository.save(시리즈(user));
        postRepository.saveAll(게시물_모음(user, series));

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.ASC, "createdAt");
        SeriesPostsFindRequest seriesPostsFindRequest = new SeriesPostsFindRequest(user.getNickname(), "시리즈");

        //when
        SliceResponse<PostSummaryResponse> sliceResponse = postQueryService.findPostsByUserAndSeries(seriesPostsFindRequest, pageRequest);

        //then
        assertAll(
                () -> assertThat(sliceResponse.data()).isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::createdAt)),
                () -> assertThat(sliceResponse.hasNext()).isFalse(),
                () -> assertThat(sliceResponse.numberOfElements()).isEqualTo(sliceResponse.data().size())
        );
    }

    @DisplayName("최신 순서로 등록된 게시물 목록을 조회할 수 있다.")
    @Test
    void findPostsInLatestOrderTest() {
        //given
        userRepository.save(유저);
        postRepository.saveAll(게시물_모음);

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");

        //when
        SliceResponse<PostSummaryResponse> sliceResponse = postQueryService.findPostsInLatestOrder(pageRequest);

        //then
        assertAll(
                () -> assertThat(sliceResponse.data()).isSortedAccordingTo(Comparator.comparing(PostSummaryResponse::createdAt).reversed()),
                () -> assertThat(sliceResponse.hasNext()).isFalse(),
                () -> assertThat(sliceResponse.numberOfElements()).isEqualTo(sliceResponse.data().size())
        );
    }

    @DisplayName("좋아요 많이 받은 순서로 게시물을 조회할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"today, week, month, year, default"})
    void findLikedPostsTest(String timePeriod) {
        //given
        User user1 = 유저();
        User user2 = 유저();
        userRepository.saveAll(List.of(user1, user2));

        Post post1 = 공개_게시물(user1, null);
        Post post2 = 공개_게시물(user1, null);
        postRepository.saveAll(List.of(post1, post2));

        PostLike like1 = 좋아요(user1, post1);
        PostLike like2 = 좋아요(user1, post2);
        PostLike like3 = 좋아요(user2, post1);
        postLikeRepository.saveAll(List.of(like1, like2, like3));

        PageRequest pageRequest = PageRequest.of(0, 30);

        //when
        SliceResponse<PostSummaryResponse> sliceResponse = postQueryService.findLikedPosts(timePeriod, pageRequest);

        //then
        assertAll(
                () -> assertThat(sliceResponse.hasNext()).isFalse(),
                () -> assertThat(sliceResponse.numberOfElements()).isEqualTo(sliceResponse.data().size()),
                () -> assertThat(sliceResponse.data().get(0).postId()).isEqualTo(post1.getId()),
                () -> assertThat(sliceResponse.data().get(1).postId()).isEqualTo(post2.getId())
        );
    }

    @DisplayName("사용자 닉네임으로 게시물리스트를 조회해 요약 리스트로 반환한다.")
    @Test
    void searchByUserNicknameTest() {
        User user = userRepository.save(유저);
        postRepository.saveAll(게시물_모음);

        int pageSize = 20;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.Direction.ASC, "createdAt");
        SliceResponse<PostSummaryResponse> sliceResponse = postQueryService.searchByNicknameAndHashtag(user.getNickname(), "", pageRequest);

        assertThat(sliceResponse.numberOfElements()).isEqualTo(pageSize);
        assertThat(sliceResponse.hasNext()).isTrue();
    }

}
