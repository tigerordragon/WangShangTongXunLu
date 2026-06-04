package com.addressbook.entity;

import java.time.Instant;

/** 表示管理员账号，对应 admin_user 表。 */
public class AdminUser {
    private final Long id;
    private final String username;
    private final String password;
    private final String displayName;
    private final AuditStatus auditStatus;
    private final String reviewedBy;
    private final Instant reviewedAt;
    private final Instant createdAt;

    /** 创建管理员对象。 */
    public AdminUser(Long id, String username, String password, String displayName,
                     AuditStatus auditStatus, String reviewedBy, Instant reviewedAt, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.auditStatus = auditStatus;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /** 判断密码是否匹配。 */
    public boolean passwordMatches(String inputPassword) {
        return password.equals(inputPassword);
    }

    /** 返回审核通过后的新对象。 */
    public AdminUser approved(String reviewer, Instant reviewedAt) {
        return new AdminUser(id, username, password, displayName, AuditStatus.APPROVED, reviewer, reviewedAt, createdAt);
    }

    /** 返回审核拒绝后的新对象。 */
    public AdminUser rejected(String reviewer, Instant reviewedAt) {
        return new AdminUser(id, username, password, displayName, AuditStatus.REJECTED, reviewer, reviewedAt, createdAt);
    }
}
