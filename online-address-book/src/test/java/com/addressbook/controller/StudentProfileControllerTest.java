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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentProfileControllerTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-06-02T12:00:00Z"), ZoneOffset.UTC);

    @Test
    public void myContactReturnsSavedProfileForAuthenticatedStudent() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String accessToken = extractJsonValue(login(mockMvc), "accessToken");

        MvcResult result = mockMvc.perform(get("/students/me/contact")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"enrollmentYear\":2022"));
        assertTrue(body.contains("\"email\":\"student-a@example.com\""));
        assertTrue(body.contains("\"contactMethod\":\"13800000000\""));
    }

    @Test
    public void saveMyContactUpdatesProfileAndCanBeReadBack() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String accessToken = extractJsonValue(login(mockMvc), "accessToken");
        String requestBody = "{"
                + "\"major\":\"软件工程\","
                + "\"className\":\"二班\","
                + "\"enrollmentYear\":2023,"
                + "\"jobUnit\":\"新公司\","
                + "\"city\":\"上海\","
                + "\"contactMethod\":\"13900000000\","
                + "\"email\":\"new@example.com\""
                + "}";

        mockMvc.perform(put("/students/me/contact")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/students/me/contact")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"enrollmentYear\":2023"));
        assertTrue(body.contains("\"email\":\"new@example.com\""));
        assertTrue(body.contains("\"contactMethod\":\"13900000000\""));
    }

    @Test
    public void myContactRejectsMissingToken() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(get("/students/me/contact"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void saveMyContactRejectsInvalidEmail() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String accessToken = extractJsonValue(login(mockMvc), "accessToken");

        mockMvc.perform(put("/students/me/contact")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"major\":\"计算机科学\",\"className\":\"一班\",\"enrollmentYear\":2022,"
                                + "\"email\":\"invalid-email\"}"))
                .andExpect(status().isBadRequest());
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
                "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "student-a@example.com"));
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
