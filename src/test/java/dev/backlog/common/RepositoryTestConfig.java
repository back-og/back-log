package dev.backlog.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.backlog.common.config.JpaConfig;
import dev.backlog.common.config.QueryDslConfig;
import dev.backlog.domain.comment.infra.CommentRepositoryAdaptor;
import dev.backlog.domain.comment.infra.jpa.CommentJpaRepository;
import dev.backlog.domain.comment.model.repository.CommentRepository;
import dev.backlog.domain.hashtag.infrastructure.HashtagRepositoryAdapter;
import dev.backlog.domain.hashtag.infrastructure.jpa.HashtagJpaRepository;
import dev.backlog.domain.hashtag.model.repository.HashtagRepository;
import dev.backlog.domain.like.infra.LikeRepositoryAdaptor;
import dev.backlog.domain.like.infra.jpa.LikeJpaRepository;
import dev.backlog.domain.like.model.repository.LikeRepository;
import dev.backlog.domain.post.infra.PostHashtagRepositoryAdaptor;
import dev.backlog.domain.post.infra.PostRepositoryAdaptor;
import dev.backlog.domain.post.infra.jpa.PostHashtagJpaRepository;
import dev.backlog.domain.post.infra.jpa.PostJpaRepository;
import dev.backlog.domain.post.infra.jpa.query.PostQueryDslRepositoryImpl;
import dev.backlog.domain.post.model.repository.PostHashtagRepository;
import dev.backlog.domain.post.model.repository.PostQueryRepository;
import dev.backlog.domain.post.model.repository.PostRepository;
import dev.backlog.domain.series.infra.SeriesRepositoryAdaptor;
import dev.backlog.domain.series.infra.jpa.SeriesJpaRepository;
import dev.backlog.domain.series.model.repository.SeriesRepository;
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
        public LikeRepository likeRepository(LikeJpaRepository likeJpaRepository) {
            return new LikeRepositoryAdaptor(likeJpaRepository);
        }

        @Bean
        public HashtagRepository hashtagRepository(HashtagJpaRepository hashtagJpaRepository) {
            return new HashtagRepositoryAdapter(hashtagJpaRepository);
        }

    }

}
