package com.addressbook.service;

import com.addressbook.entity.AuditStatus;

/** 表示学生注册结果。 */
public class StudentRegistrationResult {
    private final StudentRegistrationStatus status;
    private final Long studentId;
    private final AuditStatus auditStatus;

    /** 创建学生注册结果。 */
    private StudentRegistrationResult(StudentRegistrationStatus status, Long studentId, AuditStatus auditStatus) {
        this.status = status;
        this.studentId = studentId;
        this.auditStatus = auditStatus;
    }

    /** 创建注册成功结果。 */
    public static StudentRegistrationResult success(Long studentId) {
        return new StudentRegistrationResult(StudentRegistrationStatus.SUCCESS, studentId, AuditStatus.PENDING);
    }

    /** 创建注册失败结果。 */
    public static StudentRegistrationResult failure(StudentRegistrationStatus status) {
        return new StudentRegistrationResult(status, null, null);
    }

    /** 返回处理状态。 */
    public StudentRegistrationStatus getStatus() {
        return status;
    }

    /** 返回学生 ID。 */
    public Long getStudentId() {
        return studentId;
    }

    /** 返回审核状态。 */
    public AuditStatus getAuditStatus() {
        return auditStatus;
    }
}
