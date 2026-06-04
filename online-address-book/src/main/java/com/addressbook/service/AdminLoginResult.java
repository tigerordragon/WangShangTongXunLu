package com.addressbook.service;

import com.addressbook.entity.AdminUser;

/** 管理员登录结果。 */
public class AdminLoginResult {
    private final AdminLoginStatus status;
    private final AdminUser adminUser;

    private AdminLoginResult(AdminLoginStatus status, AdminUser adminUser) {
        this.status = status;
        this.adminUser = adminUser;
    }

    public static AdminLoginResult success(AdminUser adminUser) {
        return new AdminLoginResult(AdminLoginStatus.SUCCESS, adminUser);
    }

    public static AdminLoginResult failure(AdminLoginStatus status) {
        return new AdminLoginResult(status, null);
    }

    public AdminLoginStatus getStatus() {
        return status;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }
}
