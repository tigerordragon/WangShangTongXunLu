package com.addressbook.controller;

import com.addressbook.dto.AccessTokenResponse;
import com.addressbook.dto.LoginRequest;
import com.addressbook.dto.LoginResponse;
import com.addressbook.dto.MessageResponse;
import com.addressbook.dto.RefreshTokenRequest;
import com.addressbook.service.LoginResult;
import com.addressbook.service.LoginStatus;
import com.addressbook.service.StudentLoginService;
import com.addressbook.service.auth.AuthTokenPair;
import com.addressbook.service.auth.AuthTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

/** 处理学生登录、刷新 token 和退出登录。 */
@RestController
@RequestMapping("/auth")
public class StudentAuthController {
    private final StudentLoginService loginService;
    private final AuthTokenService tokenService;

    /** 创建学生认证控制器。 */
    public StudentAuthController(StudentLoginService loginService, AuthTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    /** 处理学生登录请求。 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResult result = loginService.login(request.getUsername(), request.getPassword());
        if (result.getStatus() == LoginStatus.INVALID_CREDENTIALS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(false, "账号或密码不正确"));
        }
        if (result.getStatus() == LoginStatus.NOT_APPROVED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(false, "账号未通过审核"));
        }
        AuthTokenPair tokenPair = result.getTokenPair();
        return ResponseEntity.ok(new LoginResponse(
                true,
                tokenPair.getAccessToken(),
                tokenPair.getRefreshToken(),
                result.getLoginCount(),
                formatInstant(result.getLastLoginTime())
        ));
    }

    /** 处理访问 token 续期请求。 */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        Optional<String> accessToken = tokenService.refreshAccessToken(request.getRefreshToken());
        if (!accessToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(false, "刷新 token 无效"));
        }
        return ResponseEntity.ok(new AccessTokenResponse(true, accessToken.get()));
    }

    /** 处理退出登录请求。 */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestBody RefreshTokenRequest request) {
        tokenService.revokeRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new MessageResponse(true, "退出成功"));
    }

    /** 格式化时间给前端展示。 */
    private String formatInstant(Instant instant) {
        return instant == null ? "" : instant.toString();
    }
}
