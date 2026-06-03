package com.addressbook.controller;

import com.addressbook.dto.MessageResponse;
import com.addressbook.dto.PendingStudentResponse;
import com.addressbook.service.StudentAuditResult;
import com.addressbook.service.StudentAuditService;
import com.addressbook.service.StudentAuditStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 处理学生审核相关接口。 */
@RestController
@RequestMapping("/admin/students")
public class StudentAuditController {
    private final StudentAuditService auditService;

    /** 创建学生审核控制器。 */
    public StudentAuditController(StudentAuditService auditService) {
        this.auditService = auditService;
    }

    /** 查询待审核学生列表。 */
    @GetMapping("/pending")
    public ResponseEntity<List<PendingStudentResponse>> listPendingStudents() {
        return ResponseEntity.ok(auditService.listPendingStudents());
    }

    /** 通过学生审核。 */
    @PostMapping("/{studentId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long studentId) {
        return handleAuditResult(auditService.approve(studentId), "approved");
    }

    /** 拒绝学生审核。 */
    @PostMapping("/{studentId}/reject")
    public ResponseEntity<?> reject(@PathVariable Long studentId) {
        return handleAuditResult(auditService.reject(studentId), "rejected");
    }

    /** 根据审核结果返回响应。 */
    private ResponseEntity<?> handleAuditResult(StudentAuditResult result, String successMessage) {
        if (result.getStatus() == StudentAuditStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "student not found"));
        }
        if (result.getStatus() == StudentAuditStatus.NOT_PENDING) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(false, "student is not pending"));
        }
        return ResponseEntity.ok(new MessageResponse(true, successMessage));
    }
}
