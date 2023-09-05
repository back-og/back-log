package dev.backlog.domain.user.api;

import dev.backlog.domain.user.dto.UserDetailsResponse;
import dev.backlog.domain.user.dto.UserResponse;
import dev.backlog.domain.user.dto.UserUpdateRequest;
import dev.backlog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{nickname}")
    public ResponseEntity<UserResponse> findUserProfile(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.findUserProfile(nickname));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> findMyProfile(@RequestHeader("Authorization") String token) {
        String realToken = token.substring(7);

        return ResponseEntity.ok(userService.findMyProfile(realToken));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody UserUpdateRequest request, Long userId) {
        userService.updateProfile(request, userId);

        return ResponseEntity.noContent().build();
    }

}
