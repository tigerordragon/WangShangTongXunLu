package com.addressbook.service.auth;

import com.addressbook.entity.RefreshTokenRecord;
import com.addressbook.repository.RefreshTokenStore;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/** 负责签发、校验和刷新双 token。 */
public class AuthTokenService {
    private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(30);
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7);
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final String secret;
    private final RefreshTokenStore refreshTokenStore;
    private final Clock clock;

    /** 创建认证 token 服务。 */
    public AuthTokenService(String secret, RefreshTokenStore refreshTokenStore, Clock clock) {
        this.secret = secret;
        this.refreshTokenStore = refreshTokenStore;
        this.clock = clock;
    }

    /** 为学生签发访问 token 和刷新 token。 */
    public AuthTokenPair issueTokenPair(Long studentId) {
        String accessToken = createAccessToken(studentId);
        String refreshToken = UUID.randomUUID().toString().replace("-", "");
        RefreshTokenRecord record = new RefreshTokenRecord(refreshToken, studentId, Instant.now(clock).plus(REFRESH_TOKEN_TTL), true);
        refreshTokenStore.save(record);
        return new AuthTokenPair(accessToken, refreshToken);
    }

    /** 校验访问 token 并返回学生身份。 */
    public Optional<AccessTokenClaims> verifyAccessToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }
        String payload = parts[0] + "." + parts[1];
        if (!sign(payload).equals(parts[2])) {
            return Optional.empty();
        }
        try {
            Long studentId = Long.valueOf(new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8));
            Instant expiresAt = Instant.ofEpochMilli(Long.parseLong(new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8)));
            if (!expiresAt.isAfter(Instant.now(clock))) {
                return Optional.empty();
            }
            return Optional.of(new AccessTokenClaims(studentId, expiresAt));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    /** 使用有效刷新 token 换取新的访问 token。 */
    public Optional<String> refreshAccessToken(String refreshToken) {
        return refreshTokenStore.find(refreshToken)
                .filter(RefreshTokenRecord::isActive)
                .filter(record -> record.getExpiresAt().isAfter(Instant.now(clock)))
                .map(record -> createAccessToken(record.getStudentId()));
    }

    /** 让刷新 token 立即失效。 */
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenStore.revoke(refreshToken);
    }

    /** 生成短期访问 token。 */
    private String createAccessToken(Long studentId) {
        String studentPart = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(String.valueOf(studentId).getBytes(StandardCharsets.UTF_8));
        String expiresPart = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(String.valueOf(Instant.now(clock).plus(ACCESS_TOKEN_TTL).toEpochMilli()).getBytes(StandardCharsets.UTF_8));
        String payload = studentPart + "." + expiresPart;
        return payload + "." + sign(payload);
    }

    /** 计算 token 签名。 */
    private String sign(String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot sign token", ex);
        }
    }
}
