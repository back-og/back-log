package dev.backlog.domain.auth.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonProperty("id")
    private Long oauthProviderId;

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private Profile profile;
        private String email;

        public Profile getProfile() {
            return profile;
        }

        public String getEmail() {
            return email;
        }
    }

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Profile {
        private String nickname;
        private String profileImage;

        public String getNickname() {
            return nickname;
        }

        public Optional<String> getProfileImage() {
            return Optional.ofNullable(profileImage);
        }
    }

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    public Optional<String> getProfileImage() {
        return kakaoAccount.getProfile().getProfileImage();
    }

    public Long getOauthProviderId() {
        return oauthProviderId;
    }

    public String getNickname() {
        return kakaoAccount.getProfile().getNickname();
    }

}
