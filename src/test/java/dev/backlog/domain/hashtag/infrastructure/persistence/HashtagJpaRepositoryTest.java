package dev.backlog.domain.hashtag.infrastructure.persistence;

import dev.backlog.common.RepositoryTest;
import dev.backlog.domain.hashtag.model.Hashtag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class HashtagJpaRepositoryTest extends RepositoryTest {

    @Autowired
    private HashtagJpaRepository hashtagJpaRepository;

    @DisplayName("해쉬태그의 이름으로 해쉬태그를 찾을 수 있다.")
    @Test
    void findByNameTest() {
        String 해쉬태그 = "해쉬태그";
        Hashtag hashtag = new Hashtag(해쉬태그);

        Hashtag savedHashtag = hashtagJpaRepository.save(hashtag);
        Hashtag foundHashtag = hashtagJpaRepository.findByName(해쉬태그).get();

        assertThat(foundHashtag).isEqualTo(savedHashtag);
    }

}