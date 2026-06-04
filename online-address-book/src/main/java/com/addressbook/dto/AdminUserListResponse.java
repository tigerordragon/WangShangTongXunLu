package com.addressbook.dto;

import java.util.List;

/** 管理员列表响应。 */
public class AdminUserListResponse {
    private final boolean success;
    private final List<AdminUserResponse> admins;

    public AdminUserListResponse(boolean success, List<AdminUserResponse> admins) {
        this.success = success;
        this.admins = admins;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<AdminUserResponse> getAdmins() {
        return admins;
    }
}
