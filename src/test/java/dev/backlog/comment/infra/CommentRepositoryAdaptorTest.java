package dev.backlog.comment.infra;

import dev.backlog.comment.domain.Comment;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.댓글1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryAdaptorTest extends RepositoryTestConfig {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("모든 댓글을 저장할 수 있다.")
    @Test
    void saveAllTest() {
        //given
        User user = userRepository.save(유저1());
        Post post = postRepository.save(공개_게시물(user, null));
        List<Comment> comments = List.of(댓글1(user, post), 댓글1(user, post));

        //when
        List<Comment> savedComments = commentRepository.saveAll(comments);

        //then
        assertThat(savedComments).containsAll(comments);
    }

    @DisplayName("특정 게시글에 달린 댓글 목록을 조회할 수 있다.")
    @Test
    void findAllByPostTest() {
        //given
        User user = userRepository.save(유저1());
        Post post = postRepository.save(공개_게시물(user, null));
        commentRepository.saveAll(List.of(댓글1(user, post), 댓글1(user, post)));

        //when
        List<Comment> comments = commentRepository.findAllByPost(post);

        //then
        int expectedSize = 2;
        assertThat(comments).hasSize(expectedSize);
    }

    @DisplayName("특정 게시글에 달린 댓글 수를 조회할 수 있다.")
    @Test
    void countByPostTest() {
        //given
        User user = userRepository.save(유저1());
        Post post = postRepository.save(공개_게시물(user, null));
        List<Comment> comments = List.of(댓글1(user, post), 댓글1(user, post));
        commentRepository.saveAll(comments);

        //when
        int count = commentRepository.countByPost(post);

        //then
        assertThat(count).isEqualTo(comments.size());
    }

    @DisplayName("모든 댓글을 삭제할 수 있다.")
    @Test
    void deleteAllTest() {
        //given
        User user = userRepository.save(유저1());
        Post post = postRepository.save(공개_게시물(user, null));
        List<Comment> comments = List.of(댓글1(user, post), 댓글1(user, post));
        commentRepository.saveAll(comments);

        //when
        commentRepository.deleteAll();

        //then
        List<Comment> foundComments = commentRepository.findAllByPost(post);
        assertThat(foundComments).isEmpty();
    }

}
