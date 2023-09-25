package dev.backlog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank
        String nickname,
        @Email
        String email,
        @NotBlank
        String profileImage,
        String introduction,
        @NotBlank
        String blogTitle
) {
}
