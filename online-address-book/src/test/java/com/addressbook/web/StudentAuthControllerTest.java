package com.addressbook.web;

import com.addressbook.auth.AuthTokenService;
import com.addressbook.auth.InMemoryRefreshTokenStore;
import com.addressbook.student.AuditStatus;
import com.addressbook.student.InMemoryStudentRepository;
import com.addressbook.student.Student;
import com.addressbook.student.StudentLoginService;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StudentAuthControllerTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-06-02T12:00:00Z"), ZoneOffset.UTC);

    @Test
    public void loginReturnsTokenPairForApprovedStudent() {
        StudentAuthController controller = createController();
        Map<String, String> request = new HashMap<String, String>();
        request.put("username", "student-a");
        request.put("password", "123456");

        JsonResponse response = controller.login(request);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("\"accessToken\""));
        assertTrue(response.getBody().contains("\"refreshToken\""));
        assertTrue(response.getBody().contains("\"loginCount\":1"));
    }

    @Test
    public void refreshReturnsNewAccessTokenForValidRefreshToken() {
        StudentAuthController controller = createController();
        Map<String, String> loginRequest = new HashMap<String, String>();
        loginRequest.put("username", "student-a");
        loginRequest.put("password", "123456");
        String refreshToken = JsonUtil.extractString(controller.login(loginRequest).getBody(), "refreshToken");
        Map<String, String> refreshRequest = new HashMap<String, String>();
        refreshRequest.put("refreshToken", refreshToken);

        JsonResponse response = controller.refresh(refreshRequest);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("\"accessToken\""));
    }

    @Test
    public void logoutRevokesRefreshToken() {
        StudentAuthController controller = createController();
        Map<String, String> loginRequest = new HashMap<String, String>();
        loginRequest.put("username", "student-a");
        loginRequest.put("password", "123456");
        String refreshToken = JsonUtil.extractString(controller.login(loginRequest).getBody(), "refreshToken");
        Map<String, String> logoutRequest = new HashMap<String, String>();
        logoutRequest.put("refreshToken", refreshToken);

        JsonResponse logoutResponse = controller.logout(logoutRequest);
        JsonResponse refreshResponse = controller.refresh(logoutRequest);

        assertEquals(200, logoutResponse.getStatusCode());
        assertEquals(401, refreshResponse.getStatusCode());
    }

    private StudentAuthController createController() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 0, null));
        InMemoryRefreshTokenStore tokenStore = new InMemoryRefreshTokenStore();
        AuthTokenService tokenService = new AuthTokenService("test-secret", tokenStore, clock);
        StudentLoginService loginService = new StudentLoginService(repository, tokenService, clock);
        return new StudentAuthController(loginService, tokenService);
    }
}
