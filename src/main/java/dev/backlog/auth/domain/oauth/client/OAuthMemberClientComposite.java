package dev.backlog.auth.domain.oauth.client;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.common.exception.InvalidAuthException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.backlog.auth.exception.AuthErrorCode.INVALID_LOGIN_TYPE;

@Component
public class OAuthMemberClientComposite {

    private final Map<OAuthProvider, OAuthMemberClient> mapping;

    public OAuthMemberClientComposite(Set<OAuthMemberClient> clients) {
        this.mapping = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthMemberClient::oAuthProvider, Function.identity())
        );
    }

    public OAuthInfoResponse fetch(OAuthProvider oAuthProvider, String authCode) {
        return getClient(oAuthProvider).fetch(authCode);
    }

    public OAuthMemberClient getClient(OAuthProvider oAuthProvider) {
        return Optional.ofNullable(mapping.get(oAuthProvider))
                .orElseThrow(() -> new InvalidAuthException(
                        INVALID_LOGIN_TYPE, INVALID_LOGIN_TYPE.getMessage()));
    }

}
