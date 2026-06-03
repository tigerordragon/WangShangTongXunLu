package com.addressbook.dto;

/** 表示待审核学生信息。 */
public class PendingStudentResponse {
    private final Long studentId;
    private final String username;
    private final String auditStatus;

    /** 创建待审核学生响应。 */
    public PendingStudentResponse(Long studentId, String username, String auditStatus) {
        this.studentId = studentId;
        this.username = username;
        this.auditStatus = auditStatus;
    }

    /** 返回学生 ID。 */
    public Long getStudentId() {
        return studentId;
    }

    /** 返回登录账号。 */
    public String getUsername() {
        return username;
    }

    /** 返回审核状态。 */
    public String getAuditStatus() {
        return auditStatus;
    }
}
