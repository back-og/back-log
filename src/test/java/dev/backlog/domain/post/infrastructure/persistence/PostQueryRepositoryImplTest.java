package dev.backlog.domain.post.infrastructure.persistence;

import dev.backlog.common.RepositoryTest;
import dev.backlog.domain.like.infrastructure.persistence.LikeRepository;
import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static dev.backlog.common.fixture.TestFixture.게시물1;
import static dev.backlog.common.fixture.TestFixture.유저1;
import static dev.backlog.common.fixture.TestFixture.좋아요1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PostQueryRepositoryImplTest extends RepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeRepository likeRepository;

    @DisplayName("오늘, 이번 주, 이번 달, 올해 필터링을 통해 좋아요 많이 받은 순서로 게시물을 조회할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"today, week, month, year, default"})
    void findLikedPostsByTimePeriod(String timePeriod) {
        //given
        User user1 = 유저1();
        User user2 = 유저1();
        userRepository.saveAll(List.of(user1, user2));

        Post post1 = 게시물1(user1, null);
        Post post2 = 게시물1(user1, null);
        postRepository.saveAll(List.of(post1, post2));

        Like like1 = 좋아요1(user1, post1);
        Like like2 = 좋아요1(user1, post2);
        Like like3 = 좋아요1(user2, post1);
        likeRepository.saveAll(List.of(like1, like2, like3));

        PageRequest pageRequest = PageRequest.of(0, 30);

        //when
        Slice<Post> postSlice = postRepository.findLikedPostsByTimePeriod(timePeriod, pageRequest);

        //then
        assertAll(
                () -> assertThat(postSlice.hasNext()).isFalse(),
                () -> assertThat(postSlice.getNumberOfElements()).isEqualTo(postSlice.getContent().size()),
                () -> assertThat(postSlice.getContent().get(0)).isEqualTo(post1),
                () -> assertThat(postSlice.getContent().get(1)).isEqualTo(post2)
        );
    }

}
