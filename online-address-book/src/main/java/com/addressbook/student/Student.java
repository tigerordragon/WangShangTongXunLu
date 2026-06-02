package com.addressbook.student;

import java.time.Instant;

/** 表示学生账号和登录状态。 */
public class Student {
    private final Long id;
    private final String username;
    private final String password;
    private final AuditStatus auditStatus;
    private final int loginCount;
    private final Instant lastLoginTime;

    /** 创建学生对象。 */
    public Student(Long id, String username, String password, AuditStatus auditStatus, int loginCount, Instant lastLoginTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.auditStatus = auditStatus;
        this.loginCount = loginCount;
        this.lastLoginTime = lastLoginTime;
    }

    /** 返回学生 ID。 */
    public Long getId() {
        return id;
    }

    /** 返回登录账号。 */
    public String getUsername() {
        return username;
    }

    /** 判断密码是否匹配。 */
    public boolean passwordMatches(String inputPassword) {
        return password.equals(inputPassword);
    }

    /** 返回审核状态。 */
    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    /** 返回累计登录次数。 */
    public int getLoginCount() {
        return loginCount;
    }

    /** 返回最近登录时间。 */
    public Instant getLastLoginTime() {
        return lastLoginTime;
    }

    /** 返回记录登录后的学生对象。 */
    public Student recordLogin(Instant loginTime) {
        return new Student(id, username, password, auditStatus, loginCount + 1, loginTime);
    }
}
