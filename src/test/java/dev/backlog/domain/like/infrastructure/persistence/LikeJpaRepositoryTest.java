package dev.backlog.domain.like.infrastructure.persistence;

import dev.backlog.common.RepositoryTest;
import dev.backlog.domain.post.infra.jpa.PostJpaRepository;
import dev.backlog.domain.post.model.Post;
import dev.backlog.domain.user.infrastructure.persistence.UserJpaRepository;
import dev.backlog.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.backlog.common.fixture.EntityFixture.게시물1;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static dev.backlog.common.fixture.EntityFixture.좋아요1;

class LikeJpaRepositoryTest extends RepositoryTest {

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PostJpaRepository postRepository;

    @DisplayName("특정 게시물에 달린 좋아요 수를 조회할 수 있다.")
    @Test
    void countByPost() {
        //given
        User user1 = userJpaRepository.save(유저1());
        User user2 = userJpaRepository.save(유저1());
        Post post = postRepository.save(게시물1(user1, null));
        likeJpaRepository.save(좋아요1(user1, post));
        likeJpaRepository.save(좋아요1(user2, post));

        //when
        int likeCount = likeJpaRepository.countByPost(post);

        //then
        int expectedCount = 2;
        Assertions.assertThat(likeCount).isEqualTo(expectedCount);
    }

}
