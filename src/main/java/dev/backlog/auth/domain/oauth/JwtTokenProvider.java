package dev.backlog.auth.domain.oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import dev.backlog.common.exception.InvalidAuthException;

import static dev.backlog.auth.exception.AuthErrorCode.AUTHENTICATION_FAILED;
import static dev.backlog.auth.exception.AuthErrorMessage.EXPIRED_TOKEN;
import static dev.backlog.auth.exception.AuthErrorMessage.FAILED_SIGNATURE_VERIFICATION;
import static dev.backlog.auth.exception.AuthErrorMessage.INVALID_FORMAT_TOKEN;
import static dev.backlog.auth.exception.AuthErrorMessage.INVALID_STRUCTURE_TOKEN;

@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean isExpiredRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new InvalidAuthException(
                    AUTHENTICATION_FAILED, EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new InvalidAuthException(
                    AUTHENTICATION_FAILED, INVALID_FORMAT_TOKEN);
        } catch (MalformedJwtException e) {
            throw new InvalidAuthException(
                    AUTHENTICATION_FAILED, INVALID_STRUCTURE_TOKEN);
        } catch (SignatureException e) {
            throw new InvalidAuthException(
                    AUTHENTICATION_FAILED, FAILED_SIGNATURE_VERIFICATION);
        }
    }

}
