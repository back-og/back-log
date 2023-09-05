package dev.backlog.domain.user.dto;

import dev.backlog.domain.user.model.User;

public record UserDetailsResponse(
        String nickname,
        String introduction,
        String profileImage,
        String blogTitle,
        String email
) {
    public static UserDetailsResponse from(User user) {
        return new UserDetailsResponse(
                user.getNickname(),
                user.getIntroduction(),
                user.getProfileImage(),
                user.getBlogTitle(),
                String.valueOf(user.getEmail())
        );
    }

}
