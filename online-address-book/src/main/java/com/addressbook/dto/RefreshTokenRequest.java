package com.addressbook.dto;

/** 表示刷新 token 请求。 */
public class RefreshTokenRequest {
    private String refreshToken;

    /** 返回刷新 token。 */
    public String getRefreshToken() {
        return refreshToken;
    }

    /** 设置刷新 token。 */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
