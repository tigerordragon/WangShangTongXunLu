package com.addressbook.dto;

import java.util.List;

/** 表示同学通讯录查询响应。*/
public class StudentContactSearchResponse {
    private final boolean success;
    private final List<StudentContactResponse> students;

    /** 创建查询响应。*/
    public StudentContactSearchResponse(boolean success, List<StudentContactResponse> students) {
        this.success = success;
        this.students = students;
    }

    /** 返回是否成功。*/
    public boolean isSuccess() {
        return success;
    }

    /** 返回通讯录列表。*/
    public List<StudentContactResponse> getStudents() {
        return students;
    }
}
