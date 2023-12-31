package dev.backlog.like.infra;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.like.domain.PostLike;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.유저;
import static dev.backlog.common.fixture.EntityFixture.좋아요;
import static org.assertj.core.api.Assertions.assertThat;

class PostLikeRepositoryAdaptorTest extends RepositoryTestConfig {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("특정 게시물에 달린 좋아요 수를 조회할 수 있다.")
    @Test
    void countByPostTest() {
        User user1 = userRepository.save(유저());
        User user2 = userRepository.save(유저());
        Post post = postRepository.save(공개_게시물(user1, null));
        List<PostLike> postLikes = List.of(좋아요(user1, post), 좋아요(user2, post));
        postLikeRepository.saveAll(postLikes);

        int likeCount = postLikeRepository.countByPost(post);

        assertThat(likeCount).isEqualTo(postLikes.size());
    }

    @DisplayName("좋아요를 저장할 수 있다.")
    @Test
    void saveTest() {
        User user1 = userRepository.save(유저());
        Post post = postRepository.save(공개_게시물(user1, null));
        PostLike postLike = 좋아요(user1, post);

        PostLike savedPostLike = postLikeRepository.save(postLike);

        assertThat(savedPostLike).isEqualTo(postLike);
    }

    @DisplayName("여러 좋아요를 저장할 수 있다.")
    @Test
    void saveAllTest() {
        User user1 = userRepository.save(유저());
        User user2 = userRepository.save(유저());
        Post post = postRepository.save(공개_게시물(user1, null));
        List<PostLike> postLikes = List.of(좋아요(user1, post), 좋아요(user2, post));

        List<PostLike> savedPostLikes = postLikeRepository.saveAll(postLikes);

        assertThat(savedPostLikes).containsAll(postLikes);
    }

    @DisplayName("모든 좋아요를 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        User user1 = userRepository.save(유저());
        User user2 = userRepository.save(유저());
        Post post = postRepository.save(공개_게시물(user1, null));
        List<PostLike> postLikes = List.of(좋아요(user1, post), 좋아요(user2, post));
        postLikeRepository.saveAll(postLikes);

        postLikeRepository.deleteAll();

        List<PostLike> foundPostLikes = postLikeRepository.findAll();
        assertThat(foundPostLikes).isEmpty();
    }

    @DisplayName("모든 좋아요를 조회할 수 있다.")
    @Test
    void findAllTest() {
        User user1 = userRepository.save(유저());
        User user2 = userRepository.save(유저());
        Post post = postRepository.save(공개_게시물(user1, null));
        List<PostLike> postLikes = List.of(좋아요(user1, post), 좋아요(user2, post));
        List<PostLike> savedPostLikes = postLikeRepository.saveAll(postLikes);

        List<PostLike> foundPostLikes = postLikeRepository.findAll();

        assertThat(foundPostLikes).containsAll(savedPostLikes);
    }

    @DisplayName("유저와 게시물ID로 PostLike를 조회할 수 있다.")
    @Test
    void findByUserAndPostIdTest() {
        User user = userRepository.save(유저());
        Post post = postRepository.save(공개_게시물(user, null));
        postLikeRepository.save(new PostLike(user, post));

        Optional<PostLike> postLike = postLikeRepository.findByUserAndPostId(user, post.getId());

        assertThat(postLike).isPresent();
    }

}

