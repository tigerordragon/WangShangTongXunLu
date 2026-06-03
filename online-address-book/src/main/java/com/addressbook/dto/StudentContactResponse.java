package com.addressbook.dto;

import com.addressbook.entity.Student;

/** 表示可公开的同学通讯录信息。*/
public class StudentContactResponse {
    private final String username;
    private final String major;
    private final String className;
    private final Integer enrollmentYear;
    private final String jobUnit;
    private final String city;
    private final String contactMethod;
    private final String email;

    /** 创建通讯录信息响应。*/
    public StudentContactResponse(String username, String major, String className, Integer enrollmentYear,
                                  String jobUnit, String city, String contactMethod, String email) {
        this.username = username;
        this.major = major;
        this.className = className;
        this.enrollmentYear = enrollmentYear;
        this.jobUnit = jobUnit;
        this.city = city;
        this.contactMethod = contactMethod;
        this.email = email;
    }

    /** 由学生对象转换为公开通讯录信息。*/
    public static StudentContactResponse from(Student student) {
        return new StudentContactResponse(
                student.getUsername(),
                student.getMajor(),
                student.getClassName(),
                student.getEnrollmentYear(),
                student.getJobUnit(),
                student.getCity(),
                student.getContactMethod(),
                student.getEmail()
        );
    }

    /** 返回用户名。*/
    public String getUsername() {
        return username;
    }

    /** 返回专业。*/
    public String getMajor() {
        return major;
    }

    /** 返回班级。*/
    public String getClassName() {
        return className;
    }

    /** 返回入学年份。*/
    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }

    /** 返回就业单位。*/
    public String getJobUnit() {
        return jobUnit;
    }

    /** 返回城市。*/
    public String getCity() {
        return city;
    }

    /** 返回联系方式。*/
    public String getContactMethod() {
        return contactMethod;
    }

    /** 返回邮箱。*/
    public String getEmail() {
        return email;
    }
}
