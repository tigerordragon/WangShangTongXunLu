package com.addressbook.controller;

import com.addressbook.dto.MessageResponse;
import com.addressbook.dto.StudentRegisterRequest;
import com.addressbook.dto.StudentRegisterResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.service.StudentRegistrationResult;
import com.addressbook.service.StudentRegistrationService;
import com.addressbook.service.StudentRegistrationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 处理学生注册接口。 */
@RestController
@RequestMapping("/students")
public class StudentRegistrationController {
    private final StudentRegistrationService registrationService;

    /** 创建学生注册控制器。 */
    public StudentRegistrationController(StudentRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /** 提交学生注册信息。 */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentRegisterRequest request) {
        StudentRegistrationResult result = registrationService.register(request.getUsername(), request.getPassword());
        if (result.getStatus() == StudentRegistrationStatus.INVALID_INPUT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "registration info is blank"));
        }
        if (result.getStatus() == StudentRegistrationStatus.DUPLICATE_USERNAME) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(false, "username already exists"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentRegisterResponse(true, result.getStudentId(), auditStatusText(result.getAuditStatus())));
    }

    /** 格式化审核状态。 */
    private String auditStatusText(AuditStatus auditStatus) {
        return auditStatus == null ? "" : auditStatus.name();
    }
}
