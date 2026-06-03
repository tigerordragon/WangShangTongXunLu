package com.addressbook.service;

import com.addressbook.dto.StudentProfileResponse;

/** 表示本人通讯录保存结果。*/
public class ProfileSaveResult {
    private final ProfileSaveStatus status;
    private final StudentProfileResponse profile;
    private final String message;

    private ProfileSaveResult(ProfileSaveStatus status, StudentProfileResponse profile, String message) {
        this.status = status;
        this.profile = profile;
        this.message = message;
    }

    /** 创建保存成功结果。*/
    public static ProfileSaveResult success(StudentProfileResponse profile) {
        return new ProfileSaveResult(ProfileSaveStatus.SUCCESS, profile, null);
    }

    /** 创建保存失败结果。*/
    public static ProfileSaveResult failure(ProfileSaveStatus status, String message) {
        return new ProfileSaveResult(status, null, message);
    }

    /** 返回保存状态。*/
    public ProfileSaveStatus getStatus() {
        return status;
    }

    /** 返回保存后的通讯录信息。*/
    public StudentProfileResponse getProfile() {
        return profile;
    }

    /** 返回失败说明。*/
    public String getMessage() {
        return message;
    }
}
