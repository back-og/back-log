package dev.backlog.user.dto;

import dev.backlog.user.domain.User;

public record UserResponse(
        String nickname,
        String introduction,
        String profileImage,
        String blogTitle
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getNickname(),
                user.getIntroduction(),
                user.getProfileImage(),
                user.getBlogTitle()
        );
    }

}
