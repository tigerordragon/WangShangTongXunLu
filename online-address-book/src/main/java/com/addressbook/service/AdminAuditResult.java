package com.addressbook.service;

/** 管理员审核操作结果。 */
public class AdminAuditResult {
    private final AdminAuditStatus status;

    private AdminAuditResult(AdminAuditStatus status) {
        this.status = status;
    }

    public static AdminAuditResult of(AdminAuditStatus status) {
        return new AdminAuditResult(status);
    }

    public AdminAuditStatus getStatus() {
        return status;
    }
}
