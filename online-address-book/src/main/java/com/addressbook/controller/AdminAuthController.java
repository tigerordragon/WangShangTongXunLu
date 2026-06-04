package com.addressbook.controller;

import com.addressbook.dto.AdminLoginResponse;
import com.addressbook.dto.AdminRegisterRequest;
import com.addressbook.dto.AdminRegisterResponse;
import com.addressbook.dto.LoginRequest;
import com.addressbook.dto.MessageResponse;
import com.addressbook.entity.AdminUser;
import com.addressbook.service.AdminAccountService;
import com.addressbook.service.AdminLoginResult;
import com.addressbook.service.AdminLoginStatus;
import com.addressbook.service.AdminRegistrationResult;
import com.addressbook.service.AdminRegistrationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 处理管理员注册与登录接口。 */
@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {
    private final AdminAccountService adminAccountService;

    /** 创建管理员认证控制器。 */
    public AdminAuthController(AdminAccountService adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

    /** 管理员注册。 */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminRegisterRequest request) {
        AdminRegistrationResult result = adminAccountService.register(
                request.getUsername(),
                request.getPassword(),
                request.getDisplayName()
        );
        if (result.getStatus() == AdminRegistrationStatus.DUPLICATE_USERNAME) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse(false, "管理员账号已存在"));
        }
        if (result.getStatus() == AdminRegistrationStatus.INVALID_INPUT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false, "账号和密码不能为空"));
        }
        return ResponseEntity.ok(new AdminRegisterResponse(true, result.getAdminId()));
    }

    /** 管理员登录。 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AdminLoginResult result = adminAccountService.login(request.getUsername(), request.getPassword());
        if (result.getStatus() == AdminLoginStatus.INVALID_CREDENTIALS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(false, "管理员账号或密码不正确"));
        }
        if (result.getStatus() == AdminLoginStatus.NOT_APPROVED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(false, "管理员账号尚未通过审核"));
        }
        AdminUser admin = result.getAdminUser();
        return ResponseEntity.ok(new AdminLoginResponse(
                true,
                admin.getId(),
                admin.getUsername(),
                admin.getDisplayName(),
                admin.getAuditStatus().name()
        ));
    }
}
