package dev.backlog.domain.auth.infrastructure.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonProperty("id")
    private Long oauthProviderId;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private Profile profile;
        private String email;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Profile {
        private String nickname;
        private String profileImage;
    }

    public String getEmail() {
        return kakaoAccount.email;
    }

    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    public String getProfileImage() {
        return kakaoAccount.profile.profileImage;
    }

    public Long getOauthProviderId() {
        return oauthProviderId;
    }
}
