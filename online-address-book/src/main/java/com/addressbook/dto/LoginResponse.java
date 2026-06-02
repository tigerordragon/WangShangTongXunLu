package com.addressbook.dto;

/** 表示学生登录成功响应。 */
public class LoginResponse {
    private final boolean success;
    private final String accessToken;
    private final String refreshToken;
    private final int loginCount;
    private final String lastLoginTime;

    /** 创建登录成功响应。 */
    public LoginResponse(boolean success, String accessToken, String refreshToken, int loginCount, String lastLoginTime) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.loginCount = loginCount;
        this.lastLoginTime = lastLoginTime;
    }

    /** 返回是否成功。 */
    public boolean isSuccess() {
        return success;
    }

    /** 返回访问 token。 */
    public String getAccessToken() {
        return accessToken;
    }

    /** 返回刷新 token。 */
    public String getRefreshToken() {
        return refreshToken;
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
