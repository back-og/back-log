package dev.backlog.auth.domain.oauth.converter;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;

public class OAuthProviderConverter implements Converter<String, OAuthProvider> {

    @Override
    public OAuthProvider convert(@NotNull String provider) {
        return OAuthProvider.from(provider);
    }

}
