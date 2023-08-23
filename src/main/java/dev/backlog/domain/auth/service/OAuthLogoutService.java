package dev.backlog.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthLogoutService {

    private static final String BEARER_TYPE = "Bearer";
    private static final String LOGOUT_URL = "/v1/user/logout";
    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    public void kakaoLogout(String accessToken) {
        String url = apiUrl + LOGOUT_URL;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", BEARER_TYPE + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("로그아웃에 실패했습니다.");
        }
    }

}
