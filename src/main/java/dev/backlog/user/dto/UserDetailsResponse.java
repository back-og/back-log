package dev.backlog.user.dto;

import dev.backlog.user.domain.Email;
import dev.backlog.user.domain.User;

public record UserDetailsResponse(
        String nickname,
        String introduction,
        String profileImage,
        String blogTitle,
        Email email
) {
    public static UserDetailsResponse from(User user) {
        return new UserDetailsResponse(
                user.getNickname(),
                user.getIntroduction(),
                user.getProfileImage(),
                user.getBlogTitle(),
                user.getEmail()
        );
    }

}
