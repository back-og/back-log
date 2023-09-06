package dev.backlog.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.backlog.common.config.JpaConfig;
import dev.backlog.common.config.QueryDslConfig;
import dev.backlog.domain.post.infra.PostHashtagRepositoryAdaptor;
import dev.backlog.domain.post.infra.PostRepositoryAdaptor;
import dev.backlog.domain.post.infra.jpa.PostHashtagJpaRepository;
import dev.backlog.domain.post.infra.jpa.PostJpaRepository;
import dev.backlog.domain.post.infra.jpa.query.PostQueryDslRepositoryImpl;
import dev.backlog.domain.post.model.repository.PostHashtagRepository;
import dev.backlog.domain.post.model.repository.PostQueryRepository;
import dev.backlog.domain.post.model.repository.PostRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(value = {QueryDslConfig.class, JpaConfig.class, RepositoryTest.QueryDslTestConfig.class})
public class RepositoryTest {

    @TestConfiguration
    public static class QueryDslTestConfig {

        @Bean
        public PostRepository postRepository(PostJpaRepository postJpaRepository) {
            return new PostRepositoryAdaptor(postJpaRepository);
        }

        @Bean
        public PostQueryRepository postQueryRepository(JPAQueryFactory jpaQueryFactory) {
            return new PostQueryDslRepositoryImpl(jpaQueryFactory);
        }

        @Bean
        public PostHashtagRepository postHashtagRepository(PostHashtagJpaRepository postHashtagJpaRepository) {
            return new PostHashtagRepositoryAdaptor(postHashtagJpaRepository);
        }

    }

}
