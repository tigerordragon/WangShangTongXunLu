package com.addressbook.dto;

import java.util.List;

/** 表示管理员学生列表响应。 */
public class AdminStudentListResponse {
    private final boolean success;
    private final List<AdminStudentResponse> students;

    /** 创建学生列表响应。 */
    public AdminStudentListResponse(boolean success, List<AdminStudentResponse> students) {
        this.success = success;
        this.students = students;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<AdminStudentResponse> getStudents() {
        return students;
    }
}
