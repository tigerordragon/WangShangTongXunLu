package com.example.auth;

import java.time.Instant;

/** 表示服务端保存的刷新 token 记录。 */
public class RefreshTokenRecord {
    private final String token;
    private final Long studentId;
    private final Instant expiresAt;
    private final boolean active;

    /** 创建刷新 token 记录。 */
    public RefreshTokenRecord(String token, Long studentId, Instant expiresAt, boolean active) {
        this.token = token;
        this.studentId = studentId;
        this.expiresAt = expiresAt;
        this.active = active;
    }

    /** 返回 token 字符串。 */
    public String getToken() {
        return token;
    }

    /** 返回学生 ID。 */
    public Long getStudentId() {
        return studentId;
    }

    /** 返回过期时间。 */
    public Instant getExpiresAt() {
        return expiresAt;
    }

    /** 返回当前 token 是否有效。 */
    public boolean isActive() {
        return active;
    }

    /** 返回失效后的新记录。 */
    public RefreshTokenRecord revoked() {
        return new RefreshTokenRecord(token, studentId, expiresAt, false);
    }
}
