package com.addressbook.controller;

import com.addressbook.entity.AdminUser;
import com.addressbook.entity.AuditStatus;
import com.addressbook.repository.InMemoryAdminUserRepository;
import com.addressbook.service.AdminAccountService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminAuditControllerTest {

    @Test
    public void approvePendingAdminByGl1() throws Exception {
        InMemoryAdminUserRepository repository = new InMemoryAdminUserRepository();
        Instant now = Instant.parse("2026-06-04T10:00:00Z");
        repository.save(new AdminUser(10L, "pending-admin", "123456", "Pending", AuditStatus.PENDING, null, null, now));
        MockMvc mockMvc = createMockMvc(repository);

        mockMvc.perform(post("/admin/audits/admins/10/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewerUsername\":\"gl1\"}"))
                .andExpect(status().isOk());

        assertEquals(AuditStatus.APPROVED, repository.findById(10L).get().getAuditStatus());
    }

    @Test
    public void listPendingAdminsIncludesPendingOnly() throws Exception {
        InMemoryAdminUserRepository repository = new InMemoryAdminUserRepository();
        Instant now = Instant.parse("2026-06-04T10:00:00Z");
        repository.save(new AdminUser(10L, "pending-admin", "123456", "Pending", AuditStatus.PENDING, null, null, now));
        MockMvc mockMvc = createMockMvc(repository);

        mockMvc.perform(get("/admin/audits/admins/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.admins[0].username").value("pending-admin"));
    }

    private MockMvc createMockMvc(InMemoryAdminUserRepository repository) {
        Clock clock = Clock.fixed(Instant.parse("2026-06-04T10:00:00Z"), ZoneId.of("UTC"));
        AdminAccountService service = new AdminAccountService(repository, clock);
        return MockMvcBuilders.standaloneSetup(new AdminAuditController(service)).build();
    }
}
