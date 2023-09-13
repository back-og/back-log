package dev.backlog.user.dto;

import dev.backlog.user.domain.User;

public record Writer(
        Long userId,
        String nickname
) {

    public static Writer from(User user) {
        return new Writer(user.getId(), user.getNickname());
    }

}
