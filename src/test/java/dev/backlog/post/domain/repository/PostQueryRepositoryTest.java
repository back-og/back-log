package dev.backlog.post.domain.repository;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.PostHashtag;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.게시물_모음;
import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static dev.backlog.common.fixture.EntityFixture.좋아요;
import static dev.backlog.common.fixture.EntityFixture.해쉬_태그_모음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PostQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostQueryRepository postQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    PostHashtagRepository postHashtagRepository;

    private User 유저;
    private Hashtag 해쉬_태그;

    @BeforeEach
    void setUp() {
        유저 = 유저();
        해쉬_태그 = 해쉬_태그_모음().get(0);
    }

    @AfterEach
    void tearDown() {
        hashtagRepository.deleteAll();
        postHashtagRepository.deleteAll();
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("오늘, 이번 주, 이번 달, 올해 필터링을 통해 좋아요 많이 받은 순서로 게시물을 조회할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"today, week, month, year, default"})
    void findLikedPostsByTimePeriodTest(String timePeriod) {
        //given
        User user1 = 유저();
        User user2 = 유저();
        userRepository.saveAll(List.of(user1, user2));

        Post post1 = 공개_게시물(user1, null);
        Post post2 = 공개_게시물(user1, null);
        postRepository.saveAll(List.of(post1, post2));

        PostLike postLike1 = 좋아요(user1, post1);
        PostLike postLike2 = 좋아요(user1, post2);
        PostLike postLike3 = 좋아요(user2, post1);
        postLikeRepository.saveAll(List.of(postLike1, postLike2, postLike3));

        PageRequest pageRequest = PageRequest.of(0, 30);

        //when
        Slice<Post> postSlice = postQueryRepository.findLikedPostsByTimePeriod(timePeriod, pageRequest);

        //then
        assertAll(
                () -> assertThat(postSlice.hasNext()).isFalse(),
                () -> assertThat(postSlice.getNumberOfElements()).isEqualTo(postSlice.getContent().size()),
                () -> assertThat(postSlice.getContent().get(0)).isEqualTo(post1),
                () -> assertThat(postSlice.getContent().get(1)).isEqualTo(post2)
        );
    }


    @DisplayName("닉네임을 통해 게시물을 조회할 수 있다.")
    @Test
    void findByNicknameTest() {
        User user = userRepository.save(유저);
        postRepository.saveAll(게시물_모음(user, null));

        int pageSize = 20;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.Direction.ASC, "createdAt");
        Slice<Post> posts = postQueryRepository.findByUserNicknameAndHashtag(user.getNickname(), null, pageRequest);

        assertThat(posts.getSize()).isEqualTo(pageSize);
        assertThat(posts.hasNext()).isTrue();
    }

    @DisplayName("해쉬태그를 통해 게시물을 조회할 수 있다.")
    @Test
    void findByHashtagTest() {
        User user = userRepository.save(유저);
        Hashtag hashtag = hashtagRepository.save(해쉬_태그);
        List<Post> savedPosts = postRepository.saveAll(게시물_모음(user, null));
        List<PostHashtag> postHashtags = createPostHashtags(hashtag, savedPosts);

        int pageSize = 20;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.Direction.ASC, "createdAt");
        Slice<Post> posts = postQueryRepository.findByUserNicknameAndHashtag(null, "해쉬태그", pageRequest);

        assertThat(posts.getNumberOfElements()).isEqualTo(postHashtags.size());
        assertThat(posts.hasNext()).isFalse();
    }

    @DisplayName("닉네임과 해쉬태그를 통해 게시물을 조회할 수 있다.")
    @Test
    void findByNicknameAndHashtagTest() {
        User user = userRepository.save(유저);
        Hashtag hashtag = hashtagRepository.save(해쉬_태그);
        List<Post> savedPosts = postRepository.saveAll(게시물_모음(user, null));
        List<PostHashtag> postHashtags = createPostHashtags(hashtag, savedPosts);

        int pageSize = 20;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.Direction.ASC, "createdAt");
        Slice<Post> posts = postQueryRepository.findByUserNicknameAndHashtag(user.getNickname(), "해쉬태그", pageRequest);

        assertThat(posts.getNumberOfElements()).isEqualTo(postHashtags.size());
        assertThat(posts.hasNext()).isFalse();
    }

    private List<PostHashtag> createPostHashtags(Hashtag hashtag, List<Post> posts) {
        List<PostHashtag> postHashtags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            postHashtags.add(new PostHashtag(hashtag, posts.get(i)));
        }
        return postHashtagRepository.saveAll(postHashtags);
    }

}
