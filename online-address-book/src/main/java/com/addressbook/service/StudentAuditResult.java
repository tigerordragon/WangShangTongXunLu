package com.addressbook.service;

import com.addressbook.entity.AuditStatus;

/** 表示学生审核结果。 */
public class StudentAuditResult {
    private final StudentAuditStatus status;
    private final AuditStatus auditStatus;

    /** 创建学生审核结果。 */
    private StudentAuditResult(StudentAuditStatus status, AuditStatus auditStatus) {
        this.status = status;
        this.auditStatus = auditStatus;
    }

    /** 创建审核成功结果。 */
    public static StudentAuditResult success(AuditStatus auditStatus) {
        return new StudentAuditResult(StudentAuditStatus.SUCCESS, auditStatus);
    }

    /** 创建审核失败结果。 */
    public static StudentAuditResult failure(StudentAuditStatus status) {
        return new StudentAuditResult(status, null);
    }

    /** 返回处理状态。 */
    public StudentAuditStatus getStatus() {
        return status;
    }

    /** 返回审核状态。 */
    public AuditStatus getAuditStatus() {
        return auditStatus;
    }
}
