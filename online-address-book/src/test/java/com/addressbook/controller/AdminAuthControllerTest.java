package com.addressbook.controller;

import com.addressbook.repository.InMemoryAdminUserRepository;
import com.addressbook.service.AdminAccountService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminAuthControllerTest {

    @Test
    public void loginApprovedAdminReturnsSessionFields() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(post("/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"gl1\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.username").value("gl1"));
    }

    @Test
    public void registerDuplicateUsernameReturnsConflict() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(post("/admin/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"gl1\",\"password\":\"123456\",\"displayName\":\"Dup\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerNewAdminReturnsSuccess() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(post("/admin/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin-new\",\"password\":\"123456\",\"displayName\":\"New Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.adminId").exists());
    }

    private MockMvc createMockMvc() {
        InMemoryAdminUserRepository repository = new InMemoryAdminUserRepository();
        Clock clock = Clock.fixed(Instant.parse("2026-06-04T10:00:00Z"), ZoneId.of("UTC"));
        AdminAccountService service = new AdminAccountService(repository, clock);
        return MockMvcBuilders.standaloneSetup(new AdminAuthController(service)).build();
    }
}
