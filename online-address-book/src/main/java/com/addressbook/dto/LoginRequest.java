package com.addressbook.dto;

/** 表示学生登录请求。 */
public class LoginRequest {
    private String username;
    private String password;

    /** 返回登录账号。 */
    public String getUsername() {
        return username;
    }

    /** 设置登录账号。 */
    public void setUsername(String username) {
        this.username = username;
    }

    /** 返回登录密码。 */
    public String getPassword() {
        return password;
    }

    /** 设置登录密码。 */
    public void setPassword(String password) {
        this.password = password;
    }
}
