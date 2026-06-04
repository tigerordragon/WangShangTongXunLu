package com.addressbook.dto;

/** 管理员审核请求（携带审核人账号）。 */
public class AdminAuditRequest {
    private String reviewerUsername;

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }
}
