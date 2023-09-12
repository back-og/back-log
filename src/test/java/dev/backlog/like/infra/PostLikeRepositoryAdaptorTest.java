package dev.backlog.like.infra;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static dev.backlog.common.fixture.EntityFixture.좋아요1;
import static org.assertj.core.api.Assertions.assertThat;

class PostLikeRepositoryAdaptorTest extends RepositoryTestConfig {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @DisplayName("특정 게시물에 달린 좋아요 수를 조회할 수 있다.")
    @Test
    void countByPostTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<PostLike> postLikes = List.of(좋아요1(user1, post), 좋아요1(user2, post));
        postLikeRepository.saveAll(postLikes);

        //when
        int likeCount = postLikeRepository.countByPost(post);

        //then
        assertThat(likeCount).isEqualTo(postLikes.size());
    }

    @DisplayName("좋아요를 저장할 수 있다.")
    @Test
    void saveTest() {
        //given
        User user1 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        PostLike postLike = 좋아요1(user1, post);

        //when
        PostLike savedPostLike = postLikeRepository.save(postLike);

        //then
        assertThat(savedPostLike).isEqualTo(postLike);
    }

    @DisplayName("여러 좋아요를 저장할 수 있다.")
    @Test
    void saveAllTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<PostLike> postLikes = List.of(좋아요1(user1, post), 좋아요1(user2, post));

        //when
        List<PostLike> savedPostLikes = postLikeRepository.saveAll(postLikes);

        //then
        assertThat(savedPostLikes).containsAll(postLikes);
    }

    @DisplayName("모든 좋아요를 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<PostLike> postLikes = List.of(좋아요1(user1, post), 좋아요1(user2, post));
        postLikeRepository.saveAll(postLikes);

        //when
        postLikeRepository.deleteAll();

        //then
        List<PostLike> foundPostLikes = postLikeRepository.findAll();
        assertThat(foundPostLikes).isEmpty();
    }

    @DisplayName("모든 좋아요를 조회할 수 있다.")
    @Test
    void findAllTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<PostLike> postLikes = List.of(좋아요1(user1, post), 좋아요1(user2, post));
        List<PostLike> savedPostLikes = postLikeRepository.saveAll(postLikes);

        //when
        List<PostLike> foundPostLikes = postLikeRepository.findAll();

        //then
        assertThat(foundPostLikes).containsAll(savedPostLikes);
    }

}

