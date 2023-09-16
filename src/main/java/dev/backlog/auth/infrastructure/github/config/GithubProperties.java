package dev.backlog.auth.infrastructure.github.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.github")
public class GithubProperties {

    private final String clientId;
    private final String redirectUrl;
    private final String clientSecret;

}
