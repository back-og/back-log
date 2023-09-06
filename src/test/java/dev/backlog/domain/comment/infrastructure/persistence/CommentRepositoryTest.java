package dev.backlog.domain.comment.infrastructure.persistence;

import dev.backlog.common.RepositoryTest;
import dev.backlog.domain.comment.model.Comment;
import dev.backlog.domain.post.infra.jpa.PostJpaRepository;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.user.infrastructure.persistence.UserRepository;
import dev.backlog.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.댓글1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostJpaRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("특정 게시글에 달린 댓글 목록을 조회할 수 있다.")
    @Test
    void findAllByPost() {
        //given
        User user = userRepository.save(유저1());

        Post post = postRepository.save(게시물1(user, null));

        commentRepository.saveAll(List.of(댓글1(user, post), 댓글1(user, post)));

        //when
        List<Comment> comments = commentRepository.findAllByPost(post);

        //then
        int expectedSize = 2;
        assertThat(comments).hasSize(expectedSize);
    }

}
