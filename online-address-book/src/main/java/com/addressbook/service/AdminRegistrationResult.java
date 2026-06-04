package com.addressbook.service;

/** 管理员注册结果。 */
public class AdminRegistrationResult {
    private final AdminRegistrationStatus status;
    private final Long adminId;

    private AdminRegistrationResult(AdminRegistrationStatus status, Long adminId) {
        this.status = status;
        this.adminId = adminId;
    }

    public static AdminRegistrationResult success(Long adminId) {
        return new AdminRegistrationResult(AdminRegistrationStatus.SUCCESS, adminId);
    }

    public static AdminRegistrationResult failure(AdminRegistrationStatus status) {
        return new AdminRegistrationResult(status, null);
    }

    public AdminRegistrationStatus getStatus() {
        return status;
    }

    public Long getAdminId() {
        return adminId;
    }
}
