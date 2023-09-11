package dev.backlog.domain.auth.api;

import dev.backlog.domain.auth.AuthTokens;
import dev.backlog.domain.auth.dto.RefreshTokenRequest;
import dev.backlog.domain.auth.model.oauth.OAuthProvider;
import dev.backlog.domain.auth.model.oauth.dto.SignupRequest;
import dev.backlog.domain.auth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v2")
public class AuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{oAuthProvider}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OAuthProvider oAuthProvider, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.getAuthCodeRequestUrl(oAuthProvider);
        response.sendRedirect(redirectUrl);

        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthTokens> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(oAuthService.signup(request));
    }

    @GetMapping("/login/{oAuthProvider}")
    public ResponseEntity<AuthTokens> login(@PathVariable OAuthProvider oAuthProvider, @RequestParam String code) {
        return ResponseEntity.ok(oAuthService.login(oAuthProvider, code));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthTokens> renew(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(oAuthService.refresh(request.refreshToken()));
    }

}
