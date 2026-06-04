package com.addressbook.service;

/** 管理员审核操作结果状态。 */
public enum AdminAuditStatus {
    SUCCESS,
    NOT_FOUND,
    NOT_PENDING,
    FORBIDDEN_REVIEWER
}
