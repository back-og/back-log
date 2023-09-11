package dev.backlog.domain.like.model.repository;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.domain.like.model.Like;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.post.model.repository.PostRepository;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static dev.backlog.common.fixture.EntityFixture.좋아요1;
import static org.assertj.core.api.Assertions.assertThat;

class LikeRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @DisplayName("특정 게시물에 달린 좋아요 수를 조회할 수 있다.")
    @Test
    void countByPostTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<Like> likes = List.of(좋아요1(user1, post), 좋아요1(user2, post));
        likeRepository.saveAll(likes);

        //when
        int likeCount = likeRepository.countByPost(post);

        //then
        assertThat(likeCount).isEqualTo(likes.size());
    }

    @DisplayName("좋아요를 저장할 수 있다.")
    @Test
    void saveTest() {
        //given
        User user1 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        Like like = 좋아요1(user1, post);

        //when
        Like savedLike = likeRepository.save(like);

        //then
        assertThat(savedLike).isEqualTo(like);
    }

    @DisplayName("여러 좋아요를 저장할 수 있다.")
    @Test
    void saveAllTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<Like> likes = List.of(좋아요1(user1, post), 좋아요1(user2, post));

        //when
        List<Like> savedLikes = likeRepository.saveAll(likes);

        //then
        assertThat(savedLikes).containsAll(likes);
    }

    @DisplayName("모든 좋아요를 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<Like> likes = List.of(좋아요1(user1, post), 좋아요1(user2, post));
        likeRepository.saveAll(likes);

        //when
        likeRepository.deleteAll();

        //then
        List<Like> foundLikes = likeRepository.findAll();
        assertThat(foundLikes).isEmpty();
    }

    @DisplayName("모든 좋아요를 조회할 수 있다.")
    @Test
    void findAllTest() {
        //given
        User user1 = userRepository.save(유저1());
        User user2 = userRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        List<Like> likes = List.of(좋아요1(user1, post), 좋아요1(user2, post));
        List<Like> savedLikes = likeRepository.saveAll(likes);

        //when
        List<Like> foundLikes = likeRepository.findAll();

        //then
        assertThat(foundLikes).containsAll(savedLikes);
    }

}
