package com.addressbook.service;

/** 表示本人通讯录保存处理状态。*/
public enum ProfileSaveStatus {
    SUCCESS,
    STUDENT_NOT_FOUND,
    INVALID_REQUEST,
    INVALID_ENROLLMENT_YEAR,
    INVALID_EMAIL
}
