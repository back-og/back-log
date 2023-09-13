package dev.backlog.post.domain.repository;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.series.domain.Series;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.게시물_모음;
import static dev.backlog.common.fixture.EntityFixture.시리즈1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static dev.backlog.common.fixture.EntityFixture.좋아요1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    private User 유저1;
    private List<Post> 게시물_모음;

    @BeforeEach
    void setUp() {
        유저1 = 유저1();
        게시물_모음 = 게시물_모음(유저1, null);
    }

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        seriesRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("공개된 게시글 중에서 특정 사용자가 남의 글에 좋아요를 누른 글들을 조회할 수 있다.")
    @Test
    void findLikedPostsByUserIdTest() {
        //given
        User user = userRepository.save(유저1);

        List<Post> posts = postRepository.saveAll(게시물_모음);
        for (Post post : posts) {
            PostLike 좋아요1 = 좋아요1(user, post);
            postLikeRepository.save(좋아요1);
        }

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");

        //when
        Slice<Post> postSlice = postRepository.findLikedPostsByUserId(user.getId(), pageRequest);

        //then
        assertAll(
                () -> assertThat(postSlice.hasNext()).isFalse(),
                () -> assertThat(postSlice.getNumberOfElements()).isEqualTo(postSlice.getContent().size())
        );
    }

    @DisplayName("사용자와 시리즈 이름으로 게시글들을 조회할 수 있다.")
    @Test
    void findAllByUserAndSeriesTest() {
        //given
        User user = userRepository.save(유저1);
        Series series = seriesRepository.save(시리즈1(user));
        postRepository.saveAll(게시물_모음(user, series));

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.Direction.ASC, "createdAt");

        //when
        Slice<Post> postSlice = postRepository.findAllByUserAndSeries(user, series, pageRequest);

        //then
        assertAll(
                () -> assertThat(postSlice.hasNext()).isFalse(),
                () -> assertThat(postSlice.getNumberOfElements()).isEqualTo(postSlice.getContent().size())
        );
    }

    // TODO: 2023/09/11 테스트 코드 작성
    @Test
    void save() {
    }

    @Test
    void saveAll() {
    }

    @Test
    void findAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteAll() {
    }

}
