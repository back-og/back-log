package dev.backlog.auth.infrastructure.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.user.domain.Email;

@JsonNaming(SnakeCaseStrategy.class)
public class GithubMemberResponse {
    @JsonProperty("id")
    Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("name")
    private String nickname;
    @JsonProperty("avatar_url")
    private String imageUrl;

    public GithubMemberResponse(Long id, String email, String nickname, String imageUrl) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public OAuthInfoResponse toOAuthInfoResponse() {
        return OAuthInfoResponse.of(
                nickname,
                imageUrl,
                new Email(email),
                OAuthProvider.GITHUB,
                String.valueOf(id)
        );
    }

}
