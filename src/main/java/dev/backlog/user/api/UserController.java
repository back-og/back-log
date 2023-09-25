package dev.backlog.user.api;

import dev.backlog.common.annotation.Login;
import dev.backlog.user.dto.AuthInfo;
import dev.backlog.user.dto.UserDetailsResponse;
import dev.backlog.user.dto.UserResponse;
import dev.backlog.user.dto.UserUpdateRequest;
import dev.backlog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{nickname}")
    public ResponseEntity<UserResponse> findUserProfile(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.findUserProfile(nickname));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> findMyProfile(@Login AuthInfo authInfo) {
        return ResponseEntity.ok(userService.findMyProfile(authInfo.userId()));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UserUpdateRequest request, @Login AuthInfo authInfo) {
        userService.updateProfile(request, authInfo.userId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@Login AuthInfo authInfo) {
        userService.deleteUser(authInfo.userId());

        return ResponseEntity.ok().build();
    }

}
