package com.addressbook.controller;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryRefreshTokenStore;
import com.addressbook.repository.InMemoryStudentRepository;
import com.addressbook.service.StudentContactService;
import com.addressbook.service.StudentLoginService;
import com.addressbook.service.StudentProfileService;
import com.addressbook.service.auth.AuthTokenService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentAuthControllerTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-06-02T12:00:00Z"), ZoneOffset.UTC);

    @Test
    public void loginReturnsTokenPairForApprovedStudent() throws Exception {
        MockMvc mockMvc = createMockMvc();

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"student-a\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"accessToken\""));
        assertTrue(body.contains("\"refreshToken\""));
        assertTrue(body.contains("\"loginCount\":1"));
    }

    @Test
    public void loginRejectsDisabledStudent() throws Exception {
        MockMvc mockMvc = createMockMvcWithDisabledStudent();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"student-disabled\",\"password\":\"123456\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void refreshReturnsNewAccessTokenForValidRefreshToken() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String refreshToken = extractJsonValue(login(mockMvc), "refreshToken");

        MvcResult result = mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"accessToken\""));
    }

    @Test
    public void logoutRevokesRefreshToken() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String refreshToken = extractJsonValue(login(mockMvc), "refreshToken");
        String requestBody = "{\"refreshToken\":\"" + refreshToken + "\"}";

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginInfoReturnsCurrentStudentInfoWithAccessToken() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String accessToken = extractJsonValue(login(mockMvc), "accessToken");

        MvcResult result = mockMvc.perform(get("/students/me/login-info")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"loginCount\":1"));
    }

    private String login(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"student-a\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private MockMvc createMockMvc() {
        InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class one", 2022, "company-a", "hangzhou", "13800000000", "student-a@example.com"));
        InMemoryRefreshTokenStore tokenStore = new InMemoryRefreshTokenStore();
        AuthTokenService tokenService = new AuthTokenService("test-secret", tokenStore, clock);
        StudentLoginService loginService = new StudentLoginService(studentRepository, tokenService, clock);
        StudentContactService contactService = new StudentContactService(studentRepository);
        StudentProfileService profileService = new StudentProfileService(studentRepository);
        return MockMvcBuilders.standaloneSetup(
                new StudentAuthController(loginService, tokenService),
                new StudentController(loginService, contactService, profileService, tokenService)
        ).build();
    }

    private MockMvc createMockMvcWithDisabledStudent() {
        InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.save(new Student(1L, "student-disabled", "123456", AuditStatus.DISABLED, 0, null,
                "computer science", "class one", 2022, "company-a", "hangzhou", "13800000000", "student-disabled@example.com"));
        InMemoryRefreshTokenStore tokenStore = new InMemoryRefreshTokenStore();
        AuthTokenService tokenService = new AuthTokenService("test-secret", tokenStore, clock);
        StudentLoginService loginService = new StudentLoginService(studentRepository, tokenService, clock);
        StudentContactService contactService = new StudentContactService(studentRepository);
        StudentProfileService profileService = new StudentProfileService(studentRepository);
        return MockMvcBuilders.standaloneSetup(
                new StudentAuthController(loginService, tokenService),
                new StudentController(loginService, contactService, profileService, tokenService)
        ).build();
    }

    private String extractJsonValue(String json, String fieldName) {
        String marker = "\"" + fieldName + "\":\"";
        int start = json.indexOf(marker) + marker.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
