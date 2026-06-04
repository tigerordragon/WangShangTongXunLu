package com.addressbook.controller;

import com.addressbook.dto.AdminAuditRequest;
import com.addressbook.dto.AdminUserListResponse;
import com.addressbook.dto.MessageResponse;
import com.addressbook.service.AdminAccountService;
import com.addressbook.service.AdminAuditResult;
import com.addressbook.service.AdminAuditStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 处理管理员注册审核接口。 */
@RestController
@RequestMapping("/admin/audits/admins")
public class AdminAuditController {
    private final AdminAccountService adminAccountService;

    /** 创建管理员审核控制器。 */
    public AdminAuditController(AdminAccountService adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

    /** 查询待审核管理员列表。 */
    @GetMapping("/pending")
    public AdminUserListResponse listPending() {
        return new AdminUserListResponse(true, adminAccountService.listPendingAdmins());
    }

    /** 通过管理员注册审核。 */
    @PostMapping("/{adminId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long adminId, @RequestBody AdminAuditRequest request) {
        return handleAuditResult(adminAccountService.approve(adminId, reviewer(request)));
    }

    /** 拒绝管理员注册审核。 */
    @PostMapping("/{adminId}/reject")
    public ResponseEntity<?> reject(@PathVariable Long adminId, @RequestBody AdminAuditRequest request) {
        return handleAuditResult(adminAccountService.reject(adminId, reviewer(request)));
    }

    private String reviewer(AdminAuditRequest request) {
        return request == null || request.getReviewerUsername() == null
                ? ""
                : request.getReviewerUsername().trim();
    }

    private ResponseEntity<?> handleAuditResult(AdminAuditResult result) {
        if (result.getStatus() == AdminAuditStatus.FORBIDDEN_REVIEWER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(false, "仅 gl1 可审核管理员注册"));
        }
        if (result.getStatus() == AdminAuditStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, "管理员不存在"));
        }
        if (result.getStatus() == AdminAuditStatus.NOT_PENDING) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse(false, "该管理员不是待审核状态"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "审核完成"));
    }
}
