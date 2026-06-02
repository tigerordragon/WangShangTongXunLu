package com.addressbook.dto;

/** 表示访问 token 响应。 */
public class AccessTokenResponse {
    private final boolean success;
    private final String accessToken;

    /** 创建访问 token 响应。 */
    public AccessTokenResponse(boolean success, String accessToken) {
        this.success = success;
        this.accessToken = accessToken;
    }

    /** 返回是否成功。 */
    public boolean isSuccess() {
        return success;
    }

    /** 返回访问 token。 */
    public String getAccessToken() {
        return accessToken;
    }
}
