package com.addressbook.controller;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import com.addressbook.service.StudentRegistrationService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentRegistrationControllerTest {

    @Test
    public void registerReturnsCreatedStudentAndPendingStatus() throws Exception {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "existing", "123456", AuditStatus.APPROVED, 0, null));
        MockMvc mockMvc = createMockMvc(repository);

        MvcResult result = mockMvc.perform(post("/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"student-new\",\"password\":\"abc123\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("\"success\":true"));
        assertTrue(body.contains("\"studentId\":3"));
        assertTrue(body.contains("\"auditStatus\":\"PENDING\""));
    }

    @Test
    public void registerRejectsDuplicateUsername() throws Exception {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "student-dup", "123456", AuditStatus.APPROVED, 0, null));
        MockMvc mockMvc = createMockMvc(repository);

        mockMvc.perform(post("/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"student-dup\",\"password\":\"abc123\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerRejectsBlankInput() throws Exception {
        MockMvc mockMvc = createMockMvc(new InMemoryStudentRepository());

        mockMvc.perform(post("/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\" \",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    private MockMvc createMockMvc(InMemoryStudentRepository repository) {
        StudentRegistrationService registrationService = new StudentRegistrationService(repository);
        return MockMvcBuilders.standaloneSetup(new StudentRegistrationController(registrationService)).build();
    }
}
