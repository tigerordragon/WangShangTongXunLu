package com.addressbook.dto;

/** 表示个人中心登录信息响应。 */
public class LoginInfoResponse {
    private final int loginCount;
    private final String lastLoginTime;

    /** 创建登录信息响应。 */
    public LoginInfoResponse(int loginCount, String lastLoginTime) {
        this.loginCount = loginCount;
        this.lastLoginTime = lastLoginTime;
    }

    /** 返回累计登录次数。 */
    public int getLoginCount() {
        return loginCount;
    }

    /** 返回最近登录时间。 */
    public String getLastLoginTime() {
        return lastLoginTime;
    }
}
