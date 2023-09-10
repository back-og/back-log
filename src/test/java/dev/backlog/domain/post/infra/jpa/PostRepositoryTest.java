package dev.backlog.domain.post.infra.jpa;

import dev.backlog.common.RepositoryTest;
import dev.backlog.domain.like.infrastructure.persistence.LikeJpaRepository;
import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.series.infrastructure.persistence.SeriesJpaRepository;
import dev.backlog.domain.series.model.Series;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
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

class PostRepositoryTest extends RepositoryTest {

    @Autowired
    private PostJpaRepository postRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private SeriesJpaRepository seriesJpaRepository;

    private User 유저1;
    private List<Post> 게시물_모음;

    @BeforeEach
    void setUp() {
        유저1 = 유저1();
        게시물_모음 = 게시물_모음(유저1, null);
    }

    @AfterEach
    void tearDown() {
        likeJpaRepository.deleteAll();
        postRepository.deleteAll();
        seriesJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @DisplayName("공개된 게시글 중에서 특정 사용자가 남의 글에 좋아요를 누른 글들을 조회할 수 있다.")
    @Test
    void findLikedPostsByUserIdTest() {
        //given
        User user = userJpaRepository.save(유저1);

        List<Post> posts = postRepository.saveAll(게시물_모음);
        for (Post post : posts) {
            Like 좋아요1 = 좋아요1(user, post);
            likeJpaRepository.save(좋아요1);
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
        User user = userJpaRepository.save(유저1);
        Series series = seriesJpaRepository.save(시리즈1(user));
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

}
