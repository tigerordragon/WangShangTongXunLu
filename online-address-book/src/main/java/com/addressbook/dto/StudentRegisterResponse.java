package com.addressbook.dto;

/** 表示学生注册响应。 */
public class StudentRegisterResponse {
    private final boolean success;
    private final Long studentId;
    private final String auditStatus;

    /** 创建注册响应。 */
    public StudentRegisterResponse(boolean success, Long studentId, String auditStatus) {
        this.success = success;
        this.studentId = studentId;
        this.auditStatus = auditStatus;
    }

    /** 返回是否成功。 */
    public boolean isSuccess() {
        return success;
    }

    /** 返回学生 ID。 */
    public Long getStudentId() {
        return studentId;
    }

    /** 返回审核状态。 */
    public String getAuditStatus() {
        return auditStatus;
    }
}
