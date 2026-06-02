package com.addressbook.auth;

/** 登录成功后返回给前端的双 token。 */
public class AuthTokenPair {
    private final String accessToken;
    private final String refreshToken;

    /** 创建双 token 响应对象。 */
    public AuthTokenPair(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /** 返回短期访问 token。 */
    public String getAccessToken() {
        return accessToken;
    }

    /** 返回长期刷新 token。 */
    public String getRefreshToken() {
        return refreshToken;
    }
}
