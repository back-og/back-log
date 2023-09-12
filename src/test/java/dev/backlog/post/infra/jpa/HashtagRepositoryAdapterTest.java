package dev.backlog.post.infra.jpa;

import dev.backlog.common.RepositoryTestConfig;
import dev.backlog.post.domain.Hashtag;
import dev.backlog.post.domain.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class HashtagRepositoryAdapterTest extends RepositoryTestConfig {

    @Autowired
    private HashtagRepository hashtagRepository;

    @DisplayName("해쉬태그의 이름으로 해쉬태그를 찾을 수 있다.")
    @Test
    void findByNameTest() {
        String 해쉬태그 = "해쉬태그";
        Hashtag hashtag = new Hashtag(해쉬태그);

        Hashtag savedHashtag = hashtagRepository.save(hashtag);
        Hashtag foundHashtag = hashtagRepository.findByName(해쉬태그).get();

        assertThat(foundHashtag).isEqualTo(savedHashtag);
    }

}
