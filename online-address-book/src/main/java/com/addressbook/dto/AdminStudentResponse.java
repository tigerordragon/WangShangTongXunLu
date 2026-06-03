package com.addressbook.dto;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;

/** 表示管理员页面中的学生信息。 */
public class AdminStudentResponse {
    private final Long id;
    private final String username;
    private final AuditStatus auditStatus;
    private final String major;
    private final String className;
    private final Integer enrollmentYear;

    /** 创建学生响应对象。 */
    public AdminStudentResponse(Long id, String username, AuditStatus auditStatus, String major, String className, Integer enrollmentYear) {
        this.id = id;
        this.username = username;
        this.auditStatus = auditStatus;
        this.major = major;
        this.className = className;
        this.enrollmentYear = enrollmentYear;
    }

    /** 由学生对象转换为响应对象。 */
    public static AdminStudentResponse from(Student student) {
        return new AdminStudentResponse(
                student.getId(),
                student.getUsername(),
                student.getAuditStatus(),
                student.getMajor(),
                student.getClassName(),
                student.getEnrollmentYear()
        );
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public String getMajor() {
        return major;
    }

    public String getClassName() {
        return className;
    }

    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }
}
