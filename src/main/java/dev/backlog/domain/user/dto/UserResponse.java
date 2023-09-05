package dev.backlog.domain.user.dto;

import dev.backlog.domain.user.model.User;

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
