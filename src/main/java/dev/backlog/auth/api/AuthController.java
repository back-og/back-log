package dev.backlog.auth.api;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.SignupRequest;
import dev.backlog.auth.dto.AuthTokens;
import dev.backlog.auth.service.OAuthService;
import dev.backlog.common.annotation.Login;
import dev.backlog.user.dto.AuthInfo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<AuthTokens> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(oAuthService.signup(request));
    }

    @GetMapping("/login/{oAuthProvider}")
    public ResponseEntity<AuthTokens> login(@PathVariable OAuthProvider oAuthProvider, @RequestParam String code) {
        return ResponseEntity.ok(oAuthService.login(oAuthProvider, code));
    }

    @PostMapping("/renew-token")
    public ResponseEntity<AuthTokens> renew(@Login AuthInfo authInfo) {
        return ResponseEntity.ok(oAuthService.renew(authInfo.userId(), authInfo.refreshToken()));
    }

}
