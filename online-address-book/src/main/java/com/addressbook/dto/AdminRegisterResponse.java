package com.addressbook.dto;

/** 管理员注册成功响应。 */
public class AdminRegisterResponse {
    private final boolean success;
    private final Long adminId;

    public AdminRegisterResponse(boolean success, Long adminId) {
        this.success = success;
        this.adminId = adminId;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getAdminId() {
        return adminId;
    }
}
