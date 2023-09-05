package dev.backlog.domain.user.dto;

public record UserUpdateRequest(
        String nickname,
        String email,
        String profileImage,
        String introduction,
        String blogTitle
) {
}

