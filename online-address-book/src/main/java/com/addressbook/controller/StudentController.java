package com.addressbook.controller;

import com.addressbook.dto.LoginInfoResponse;
import com.addressbook.dto.MessageResponse;
import com.addressbook.dto.StudentContactSearchResponse;
import com.addressbook.dto.StudentContactResponse;
import com.addressbook.dto.StudentProfileRequest;
import com.addressbook.dto.StudentProfileResponse;
import com.addressbook.service.LoginInfo;
import com.addressbook.service.StudentContactService;
import com.addressbook.service.StudentLoginService;
import com.addressbook.service.StudentProfileService;
import com.addressbook.service.auth.AccessTokenClaims;
import com.addressbook.service.auth.AuthTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** 处理学生个人信息和同学通讯录相关接口。*/
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentLoginService loginService;
    private final StudentContactService contactService;
    private final StudentProfileService profileService;
    private final AuthTokenService tokenService;

    /** 创建学生控制器。*/
    public StudentController(StudentLoginService loginService, StudentContactService contactService,
                             StudentProfileService profileService, AuthTokenService tokenService) {
        this.loginService = loginService;
        this.contactService = contactService;
        this.profileService = profileService;
        this.tokenService = tokenService;
    }

    /** 查询当前学生的登录信息。*/
    @GetMapping("/me/login-info")
    public ResponseEntity<?> loginInfo(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Optional<AccessTokenClaims> claims = tokenService.verifyAccessToken(extractBearerToken(authorization));
        if (!claims.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(false, "访问 token 无效"));
        }
        Optional<LoginInfo> loginInfo = loginService.getLoginInfo(claims.get().getStudentId());
        if (!loginInfo.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "学生不存在"));
        }
        LoginInfo info = loginInfo.get();
        return ResponseEntity.ok(new LoginInfoResponse(info.getLoginCount(), formatInstant(info.getLastLoginTime())));
    }

    /** 查询本人通讯录信息。*/
    @GetMapping("/me/contact")
    public ResponseEntity<?> myContact(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Optional<AccessTokenClaims> claims = tokenService.verifyAccessToken(extractBearerToken(authorization));
        if (!claims.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(false, "访问 token 无效"));
        }
        Optional<StudentProfileResponse> profile = profileService.getProfile(claims.get().getStudentId());
        if (!profile.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "学生不存在"));
        }
        return ResponseEntity.ok(profile.get());
    }

    /** 保存本人通讯录信息。*/
    @PutMapping("/me/contact")
    public ResponseEntity<?> saveMyContact(@RequestHeader(value = "Authorization", required = false) String authorization,
                                           @RequestBody StudentProfileRequest request) {
        Optional<AccessTokenClaims> claims = tokenService.verifyAccessToken(extractBearerToken(authorization));
        if (!claims.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(false, "访问 token 无效"));
        }
        Optional<StudentProfileResponse> saved = profileService.saveProfile(
                claims.get().getStudentId(),
                request.getMajor(),
                request.getClassName(),
                request.getEnrollmentYear(),
                request.getJobUnit(),
                request.getCity(),
                request.getContactMethod(),
                request.getEmail()
        );
        if (!saved.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "学生不存在"));
        }
        return ResponseEntity.ok(saved.get());
    }

    /** 查询其他同学的通讯录信息。*/
    @GetMapping("/contacts")
    public ResponseEntity<?> contacts(@RequestHeader(value = "Authorization", required = false) String authorization,
                                      @RequestParam(value = "major", required = false) String major,
                                      @RequestParam(value = "className", required = false) String className,
                                      @RequestParam(value = "enrollmentYear", required = false) Integer enrollmentYear) {
        Optional<AccessTokenClaims> claims = tokenService.verifyAccessToken(extractBearerToken(authorization));
        if (!claims.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(false, "访问 token 无效"));
        }
        List<StudentContactResponse> students = contactService.search(
                claims.get().getStudentId(),
                normalize(major),
                normalize(className),
                enrollmentYear
        );
        return ResponseEntity.ok(new StudentContactSearchResponse(true, students));
    }

    /** 从请求头提取 Bearer token。*/
    private String extractBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring("Bearer ".length());
    }

    /** 格式化时间给前端展示。*/
    private String formatInstant(Instant instant) {
        return instant == null ? "" : instant.toString();
    }

    /** 归一化查询条件。*/
    private String normalize(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}
