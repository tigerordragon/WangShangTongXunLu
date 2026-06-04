package com.addressbook.dto;

import com.addressbook.entity.AdminUser;

import java.time.Instant;

/** 管理员信息响应。 */
public class AdminUserResponse {
    private final Long id;
    private final String username;
    private final String displayName;
    private final String auditStatus;
    private final String createdAt;
    private final String reviewedBy;
    private final String reviewedAt;

    public AdminUserResponse(Long id, String username, String displayName, String auditStatus,
                             String createdAt, String reviewedBy, String reviewedAt) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.auditStatus = auditStatus;
        this.createdAt = createdAt;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
    }

    public static AdminUserResponse from(AdminUser adminUser) {
        return new AdminUserResponse(
                adminUser.getId(),
                adminUser.getUsername(),
                adminUser.getDisplayName(),
                adminUser.getAuditStatus().name(),
                formatInstant(adminUser.getCreatedAt()),
                adminUser.getReviewedBy() == null ? "" : adminUser.getReviewedBy(),
                formatInstant(adminUser.getReviewedAt())
        );
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public String getReviewedAt() {
        return reviewedAt;
    }

    private static String formatInstant(Instant instant) {
        return instant == null ? "" : instant.toString();
    }
}
