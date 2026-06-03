package com.addressbook.controller;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import com.addressbook.service.StudentAuditService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentAuditControllerTest {

    @Test
    public void pendingListReturnsOnlyPendingStudents() throws Exception {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "pending-a", "123456", AuditStatus.PENDING, 0, null));
        repository.save(new Student(3L, "approved-a", "123456", AuditStatus.APPROVED, 0, null));
        repository.save(new Student(4L, "rejected-a", "123456", AuditStatus.REJECTED, 0, null));
        MockMvc mockMvc = createMockMvc(repository);

        MvcResult result = mockMvc.perform(get("/admin/audits/students/pending"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"studentId\":2"));
        assertTrue(body.contains("\"username\":\"pending-a\""));
        assertFalse(body.contains("\"studentId\":3"));
        assertFalse(body.contains("\"studentId\":4"));
    }

    @Test
    public void approveReturnsOkAndUpdatesStatus() throws Exception {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "pending-a", "123456", AuditStatus.PENDING, 0, null));
        MockMvc mockMvc = createMockMvc(repository);

        mockMvc.perform(post("/admin/audits/students/2/approve").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertTrue(repository.findById(2L).isPresent());
        assertEquals(AuditStatus.APPROVED, repository.findById(2L).get().getAuditStatus());
    }

    @Test
    public void rejectReturnsOkAndUpdatesStatus() throws Exception {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "pending-a", "123456", AuditStatus.PENDING, 0, null));
        MockMvc mockMvc = createMockMvc(repository);

        mockMvc.perform(post("/admin/audits/students/2/reject").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertTrue(repository.findById(2L).isPresent());
        assertEquals(AuditStatus.REJECTED, repository.findById(2L).get().getAuditStatus());
    }

    private MockMvc createMockMvc(InMemoryStudentRepository repository) {
        StudentAuditService auditService = new StudentAuditService(repository);
        return MockMvcBuilders.standaloneSetup(new StudentAuditController(auditService)).build();
    }
}
