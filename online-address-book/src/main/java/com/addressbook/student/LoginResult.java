package com.addressbook.student;

import com.addressbook.auth.AuthTokenPair;

import java.time.Instant;

/** 表示学生登录结果。 */
public class LoginResult {
    private final LoginStatus status;
    private final AuthTokenPair tokenPair;
    private final int loginCount;
    private final Instant lastLoginTime;

    /** 创建登录结果对象。 */
    private LoginResult(LoginStatus status, AuthTokenPair tokenPair, int loginCount, Instant lastLoginTime) {
        this.status = status;
        this.tokenPair = tokenPair;
        this.loginCount = loginCount;
        this.lastLoginTime = lastLoginTime;
    }

    /** 创建登录成功结果。 */
    public static LoginResult success(AuthTokenPair tokenPair, int loginCount, Instant lastLoginTime) {
        return new LoginResult(LoginStatus.SUCCESS, tokenPair, loginCount, lastLoginTime);
    }

    /** 创建登录失败结果。 */
    public static LoginResult failure(LoginStatus status) {
        return new LoginResult(status, null, 0, null);
    }

    /** 返回登录状态。 */
    public LoginStatus getStatus() {
        return status;
    }

    /** 返回双 token。 */
    public AuthTokenPair getTokenPair() {
        return tokenPair;
    }

    /** 返回是否包含双 token。 */
    public boolean hasTokenPair() {
        return tokenPair != null;
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
