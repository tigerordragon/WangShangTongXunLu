package com.addressbook.entity;

import java.time.Instant;

/** 表示学生账号和登录状态。*/
public class Student {
    private final Long id;
    private final String username;
    private final String password;
    private final AuditStatus auditStatus;
    private final int loginCount;
    private final Instant lastLoginTime;
    private final String major;
    private final String className;
    private final Integer enrollmentYear;
    private final String jobUnit;
    private final String city;
    private final String contactMethod;
    private final String email;

    /** 创建学生对象。*/
    public Student(Long id, String username, String password, AuditStatus auditStatus, int loginCount, Instant lastLoginTime) {
        this(id, username, password, auditStatus, loginCount, lastLoginTime, null, null, null, null, null, null, null);
    }

    /** 创建包含通讯录信息的学生对象。*/
    public Student(Long id, String username, String password, AuditStatus auditStatus, int loginCount, Instant lastLoginTime,
                   String major, String className, Integer enrollmentYear, String jobUnit, String city, String contactMethod, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.auditStatus = auditStatus;
        this.loginCount = loginCount;
        this.lastLoginTime = lastLoginTime;
        this.major = major;
        this.className = className;
        this.enrollmentYear = enrollmentYear;
        this.jobUnit = jobUnit;
        this.city = city;
        this.contactMethod = contactMethod;
        this.email = email;
    }

    /** 返回学生 ID。*/
    public Long getId() {
        return id;
    }

    /** 返回登录账号。*/
    public String getUsername() {
        return username;
    }

    /** 返回存储的登录密码（供持久化层写入数据库）。 */
    public String getPassword() {
        return password;
    }

    /** 判断密码是否匹配。*/
    public boolean passwordMatches(String inputPassword) {
        return password.equals(inputPassword);
    }

    /** 返回审核状态。*/
    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    /** 返回指定审核状态的新对象。 */
    public Student withAuditStatus(AuditStatus newAuditStatus) {
        return new Student(id, username, password, newAuditStatus, loginCount, lastLoginTime,
                major, className, enrollmentYear, jobUnit, city, contactMethod, email);
    }

    /** 返回累计登录次数。 */
    public int getLoginCount() {
        return loginCount;
    }

    /** 返回最近登录时间。*/
    public Instant getLastLoginTime() {
        return lastLoginTime;
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

    /** 返回记录登录后的学生对象。*/
    public Student recordLogin(Instant loginTime) {
        return new Student(id, username, password, auditStatus, loginCount + 1, loginTime,
                major, className, enrollmentYear, jobUnit, city, contactMethod, email);
    }

    /** 返回更新通讯录信息后的学生对象。*/
    public Student updateContact(String major, String className, Integer enrollmentYear,
                               String jobUnit, String city, String contactMethod, String email) {
        return new Student(id, username, password, auditStatus, loginCount, lastLoginTime,
                major, className, enrollmentYear, jobUnit, city, contactMethod, email);
    }
}
