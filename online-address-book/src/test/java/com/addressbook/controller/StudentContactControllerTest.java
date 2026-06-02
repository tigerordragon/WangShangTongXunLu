package com.addressbook.controller;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryRefreshTokenStore;
import com.addressbook.repository.InMemoryStudentRepository;
import com.addressbook.service.StudentContactService;
import com.addressbook.service.StudentLoginService;
import com.addressbook.service.auth.AuthTokenService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentContactControllerTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-06-02T12:00:00Z"), ZoneOffset.UTC);

    @Test
    public void contactsReturnsOnlyApprovedOtherStudentsMatchingFilters() throws Exception {
        MockMvc mockMvc = createMockMvc();
        String accessToken = extractJsonValue(login(mockMvc), "accessToken");

        MvcResult result = mockMvc.perform(get("/students/contacts")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("major", "计算机科学")
                        .param("className", "一班")
                        .param("enrollmentYear", "2022"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"success\":true"));
        assertTrue(body.contains("\"username\":\"student-b\""));
        assertFalse(body.contains("\"username\":\"student-a\""));
        assertFalse(body.contains("\"username\":\"student-c\""));
        assertFalse(body.contains("\"username\":\"student-d\""));
    }

    @Test
    public void contactsRejectsMissingToken() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(get("/students/contacts"))
                .andExpect(status().isUnauthorized());
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
        studentRepository.save(new Student(2L, "student-b", "123456", AuditStatus.APPROVED, 0, null,
                "计算机科学", "一班", 2022, "B公司", "上海", "13811111111", "student-b@example.com"));
        studentRepository.save(new Student(3L, "student-c", "123456", AuditStatus.APPROVED, 0, null,
                "软件工程", "一班", 2022, "C公司", "北京", "13822222222", "student-c@example.com"));
        studentRepository.save(new Student(4L, "student-d", "123456", AuditStatus.PENDING, 0, null,
                "计算机科学", "一班", 2022, "D公司", "深圳", "13833333333", "student-d@example.com"));
        InMemoryRefreshTokenStore tokenStore = new InMemoryRefreshTokenStore();
        AuthTokenService tokenService = new AuthTokenService("test-secret", tokenStore, clock);
        StudentLoginService loginService = new StudentLoginService(studentRepository, tokenService, clock);
        StudentContactService contactService = new StudentContactService(studentRepository);
        return MockMvcBuilders.standaloneSetup(
                new StudentAuthController(loginService, tokenService),
                new StudentController(loginService, contactService, tokenService)
        ).build();
    }

    private String extractJsonValue(String json, String fieldName) {
        String marker = "\"" + fieldName + "\":\"";
        int start = json.indexOf(marker) + marker.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
