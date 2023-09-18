package dev.backlog.common.config;

import dev.backlog.post.service.query.PostAccessStrategy;
import dev.backlog.post.service.query.PrivatePostAccessStrategy;
import dev.backlog.post.service.query.PublicPostAccessStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PostAccessStrategyMapConfig {

    private final PublicPostAccessStrategy publicPostAccessStrategy;
    private final PrivatePostAccessStrategy privatePostAccessStrategy;

    @Bean
    public Map<Boolean, PostAccessStrategy> postAccessStrategyMap() {
        Map<Boolean, PostAccessStrategy> postAccessStrategyMap = new HashMap<>();
        postAccessStrategyMap.put(true, publicPostAccessStrategy);
        postAccessStrategyMap.put(false, privatePostAccessStrategy);
        return postAccessStrategyMap;
    }

}
