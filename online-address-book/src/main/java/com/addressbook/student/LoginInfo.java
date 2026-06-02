package com.addressbook.student;

import java.time.Instant;

/** 表示学生个人中心展示的登录信息。 */
public class LoginInfo {
    private final int loginCount;
    private final Instant lastLoginTime;

    /** 创建登录信息对象。 */
    public LoginInfo(int loginCount, Instant lastLoginTime) {
        this.loginCount = loginCount;
        this.lastLoginTime = lastLoginTime;
    }

    /** 返回累计登录次数。 */
    public int getLoginCount() {
        return loginCount;
    }

    /** 返回最近登录时间。 */
    public Instant getLastLoginTime() {
        return lastLoginTime;
    }
}
