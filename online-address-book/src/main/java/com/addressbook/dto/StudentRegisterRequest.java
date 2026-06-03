package com.addressbook.dto;

/** 表示学生注册请求。 */
public class StudentRegisterRequest {
    private String username;
    private String password;

    /** 返回注册账号。 */
    public String getUsername() {
        return username;
    }

    /** 设置注册账号。 */
    public void setUsername(String username) {
        this.username = username;
    }

    /** 返回注册密码。 */
    public String getPassword() {
        return password;
    }

    /** 设置注册密码。 */
    public void setPassword(String password) {
        this.password = password;
    }
}
