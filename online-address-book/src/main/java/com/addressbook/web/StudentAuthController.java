package com.addressbook.web;

import com.addressbook.auth.AuthTokenPair;
import com.addressbook.auth.AuthTokenService;
import com.addressbook.student.LoginResult;
import com.addressbook.student.LoginStatus;
import com.addressbook.student.StudentLoginService;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** 处理学生登录、刷新 token 和退出登录。 */
public class StudentAuthController {
    private final StudentLoginService loginService;
    private final AuthTokenService tokenService;

    /** 创建学生认证控制器。 */
    public StudentAuthController(StudentLoginService loginService, AuthTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    /** 处理学生登录请求。 */
    public JsonResponse login(Map<String, String> request) {
        LoginResult result = loginService.login(request.get("username"), request.get("password"));
        if (result.getStatus() == LoginStatus.INVALID_CREDENTIALS) {
            return new JsonResponse(401, JsonUtil.error("账号或密码不正确"));
        }
        if (result.getStatus() == LoginStatus.NOT_APPROVED) {
            return new JsonResponse(403, JsonUtil.error("账号未通过审核"));
        }
        AuthTokenPair tokenPair = result.getTokenPair();
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("success", true);
        values.put("accessToken", tokenPair.getAccessToken());
        values.put("refreshToken", tokenPair.getRefreshToken());
        values.put("loginCount", result.getLoginCount());
        values.put("lastLoginTime", formatInstant(result.getLastLoginTime()));
        return new JsonResponse(200, JsonUtil.object(values));
    }

    /** 处理访问 token 续期请求。 */
    public JsonResponse refresh(Map<String, String> request) {
        Optional<String> accessToken = tokenService.refreshAccessToken(request.get("refreshToken"));
        if (!accessToken.isPresent()) {
            return new JsonResponse(401, JsonUtil.error("刷新 token 无效"));
        }
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("success", true);
        values.put("accessToken", accessToken.get());
        return new JsonResponse(200, JsonUtil.object(values));
    }

    /** 处理退出登录请求。 */
    public JsonResponse logout(Map<String, String> request) {
        tokenService.revokeRefreshToken(request.get("refreshToken"));
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.put("success", true);
        values.put("message", "退出成功");
        return new JsonResponse(200, JsonUtil.object(values));
    }

    /** 格式化时间给前端展示。 */
    private String formatInstant(Instant instant) {
        return instant == null ? "" : instant.toString();
    }
}
