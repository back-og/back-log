package dev.backlog.post.infra.jpa;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.Post;
import dev.backlog.post.domain.PostHashtag;
import dev.backlog.post.domain.repository.HashtagRepository;
import dev.backlog.post.domain.repository.PostHashtagRepository;
import dev.backlog.user.domain.User;
import dev.backlog.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static dev.backlog.common.fixture.EntityFixture.공개_게시물;
import static dev.backlog.common.fixture.EntityFixture.유저1;
import static dev.backlog.common.fixture.EntityFixture.해쉬태그_모음;
import static org.assertj.core.api.Assertions.assertThat;

// TODO: 2023/09/11 테스트 경로 수정 및 분리
class PostHashtagJpaRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostJpaRepository postRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    private User 유저1;
    private Post 게시물1;
    private List<Hashtag> 해쉬태그_모음;

    @BeforeEach
    void setUp() {
        유저1 = userRepository.save(유저1());
        게시물1 = postRepository.save(공개_게시물(유저1, null));
        해쉬태그_모음 = hashtagRepository.saveAll(해쉬태그_모음());
    }

    @DisplayName("PostHashtag에서 Post를 찾아 PostHashtag를 삭제한다.")
    @Test
    void deleteAllByPostTest() {
        List<PostHashtag> postHashtags = createPostHashtags(게시물1, 해쉬태그_모음);
        postHashtagRepository.saveAll(postHashtags);

        postHashtagRepository.deleteAllByPost(게시물1);

        List<PostHashtag> findPostHashtag = postHashtagRepository.findByPost(게시물1);
        assertThat(findPostHashtag).isEmpty();
    }

    @DisplayName("게시물로 게시물에 등록된 해쉬태그를 검색할 수 있다.")
    @Test
    void findByPostTest() {
        List<PostHashtag> postHashtags = createPostHashtags(게시물1, 해쉬태그_모음);
        postHashtagRepository.saveAll(postHashtags);

        List<PostHashtag> 포스트_해쉬태그 = postHashtagRepository.findByPost(게시물1);

        포스트_해쉬태그.forEach(a -> System.out.println(a.getHashtag()));
        assertThat(포스트_해쉬태그).hasSize(해쉬태그_모음.size());
    }

    @DisplayName("해쉬태그를 통해 사용되고 있는 게시물이 없다면 True를 반환한다.")
    @Test
    void existsByHashtagTest() {
        List<PostHashtag> postHashtags = createPostHashtags(게시물1, 해쉬태그_모음);
        postHashtagRepository.saveAll(postHashtags);

        Hashtag hashtag = 해쉬태그_모음.get(0);
        boolean 해쉬태그_유무 = postHashtagRepository.existsByHashtag(hashtag);
        assertThat(해쉬태그_유무).isTrue();
    }

    private List<PostHashtag> createPostHashtags(Post post, List<Hashtag> hashtags) {
        return hashtags.stream()
                .map(tag -> new PostHashtag(tag, post))
                .toList();
    }

}
