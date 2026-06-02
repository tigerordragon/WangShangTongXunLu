package com.addressbook.auth;

import java.time.Instant;

/** 表示访问 token 中解析出的学生身份和过期时间。 */
public class AccessTokenClaims {
    private final Long studentId;
    private final Instant expiresAt;

    /** 创建访问 token 声明对象。 */
    public AccessTokenClaims(Long studentId, Instant expiresAt) {
        this.studentId = studentId;
        this.expiresAt = expiresAt;
    }

    /** 返回学生 ID。 */
    public Long getStudentId() {
        return studentId;
    }

    /** 返回过期时间。 */
    public Instant getExpiresAt() {
        return expiresAt;
    }
}
