package dev.backlog.domain.auth.api;

import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoLoginParams;
import dev.backlog.domain.auth.infrastructure.kakao.dto.KakaoSignUpParams;
import dev.backlog.domain.auth.model.AuthTokens;
import dev.backlog.domain.auth.service.OAuthLoginService;
import dev.backlog.domain.auth.service.OAuthLogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final OAuthLoginService oAuthLoginService;
    private final OAuthLogoutService oAuthLogoutService;

    @PostMapping("/signup/kakao")
    public ResponseEntity<AuthTokens> kakaoSignUp(@RequestBody KakaoSignUpParams params) {
        return ResponseEntity.ok(oAuthLoginService.kakaoSignUp(params));
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<AuthTokens> kakaoLogin(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.kakaoLogin(params));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> request) {
        String accessToken = request.get("access_token");
        oAuthLogoutService.kakaoLogout(accessToken);
        return ResponseEntity.ok().build();
    }
}
