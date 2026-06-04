package com.addressbook.dto;

/** 管理员登录成功响应。 */
public class AdminLoginResponse {
    private final boolean success;
    private final Long id;
    private final String username;
    private final String displayName;
    private final String auditStatus;

    public AdminLoginResponse(boolean success, Long id, String username, String displayName, String auditStatus) {
        this.success = success;
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.auditStatus = auditStatus;
    }

    public boolean isSuccess() {
        return success;
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
}
