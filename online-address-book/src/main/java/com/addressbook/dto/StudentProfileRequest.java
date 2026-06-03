package com.addressbook.dto;

/** 表示保存本人通讯录信息的请求。*/
public class StudentProfileRequest {
    private String major;
    private String className;
    private Integer enrollmentYear;
    private String jobUnit;
    private String city;
    private String contactMethod;
    private String email;

    /** 返回专业。*/
    public String getMajor() {
        return major;
    }

    /** 设置专业。*/
    public void setMajor(String major) {
        this.major = major;
    }

    /** 返回班级。*/
    public String getClassName() {
        return className;
    }

    /** 设置班级。*/
    public void setClassName(String className) {
        this.className = className;
    }

    /** 返回入学年份。*/
    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }

    /** 设置入学年份。*/
    public void setEnrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    /** 返回就业单位。*/
    public String getJobUnit() {
        return jobUnit;
    }

    /** 设置就业单位。*/
    public void setJobUnit(String jobUnit) {
        this.jobUnit = jobUnit;
    }

    /** 返回城市。*/
    public String getCity() {
        return city;
    }

    /** 设置城市。*/
    public void setCity(String city) {
        this.city = city;
    }

    /** 返回联系方式。*/
    public String getContactMethod() {
        return contactMethod;
    }

    /** 设置联系方式。*/
    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    /** 返回邮箱。*/
    public String getEmail() {
        return email;
    }

    /** 设置邮箱。*/
    public void setEmail(String email) {
        this.email = email;
    }
}
