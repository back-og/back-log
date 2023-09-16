package dev.backlog.auth.domain.oauth;

import static java.util.Locale.ENGLISH;

public enum OAuthProvider {
    KAKAO,
    GITHUB
    ;

    public static OAuthProvider from(String type) {
        return OAuthProvider.valueOf(type.toUpperCase(ENGLISH));
    }

}
