package dev.backlog.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.backlog.comment.domain.repository.CommentRepository;
import dev.backlog.comment.infra.CommentRepositoryAdaptor;
import dev.backlog.comment.infra.jpa.CommentJpaRepository;
import dev.backlog.common.config.JpaConfig;
import dev.backlog.common.config.QueryDslConfig;
import dev.backlog.like.domain.repository.PostLikeRepository;
import dev.backlog.like.infra.PostLikeRepositoryAdaptor;
import dev.backlog.like.infra.jpa.PostLikeJpaRepository;
import dev.backlog.post.domain.repository.HashtagRepository;
import dev.backlog.post.domain.repository.PostHashtagRepository;
import dev.backlog.post.domain.repository.PostQueryRepository;
import dev.backlog.post.domain.repository.PostRepository;
import dev.backlog.post.infra.HashtagRepositoryAdapter;
import dev.backlog.post.infra.PostHashtagRepositoryAdaptor;
import dev.backlog.post.infra.PostRepositoryAdaptor;
import dev.backlog.post.infra.jpa.HashtagJpaRepository;
import dev.backlog.post.infra.jpa.PostHashtagJpaRepository;
import dev.backlog.post.infra.jpa.PostJpaRepository;
import dev.backlog.post.infra.jpa.query.PostQueryDslRepositoryImpl;
import dev.backlog.series.domain.repository.SeriesRepository;
import dev.backlog.series.infra.SeriesRepositoryAdaptor;
import dev.backlog.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.user.domain.repository.UserRepository;
import dev.backlog.user.infrastructure.UserRepositoryAdapter;
import dev.backlog.user.infrastructure.jpa.UserJpaRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(value = {QueryDslConfig.class, JpaConfig.class, RepositoryTestConfig.RepositoryAdaptorTestConfig.class})
public class RepositoryTestConfig {

    @TestConfiguration
    static class RepositoryAdaptorTestConfig {

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

        @Bean
        public SeriesRepository seriesRepository(SeriesJpaRepository seriesJpaRepository) {
            return new SeriesRepositoryAdaptor(seriesJpaRepository);
        }

        @Bean
        public CommentRepository commentRepository(CommentJpaRepository commentJpaRepository) {
            return new CommentRepositoryAdaptor(commentJpaRepository);
        }

        @Bean
        public PostLikeRepository postLikeRepository(PostLikeJpaRepository postLikeJpaRepository) {
            return new PostLikeRepositoryAdaptor(postLikeJpaRepository);
        }

        @Bean
        public HashtagRepository hashtagRepository(HashtagJpaRepository hashtagJpaRepository) {
            return new HashtagRepositoryAdapter(hashtagJpaRepository);
        }

        @Bean
        public UserRepository userRepository(UserJpaRepository userJpaRepository) {
            return new UserRepositoryAdapter(userJpaRepository);
        }

    }

}
