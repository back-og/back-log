package dev.backlog.common.config;

import dev.backlog.common.UserArgumentResolver;
import dev.backlog.domain.auth.model.oauth.JwtTokenProvider;
import dev.backlog.domain.auth.model.oauth.converter.OAuthProviderConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthWebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OAuthProviderConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver(jwtTokenProvider));
    }

}
