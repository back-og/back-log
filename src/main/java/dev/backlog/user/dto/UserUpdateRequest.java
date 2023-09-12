package dev.backlog.user.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
        String nickname,
        @Email String email,
        String profileImage,
        String introduction,
        String blogTitle
) {
}
