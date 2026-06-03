package com.addressbook.controller;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Professional;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryProfessionalRepository;
import com.addressbook.repository.InMemoryStudentRepository;
import com.addressbook.service.AdminStudentService;
import com.addressbook.service.ProfessionalService;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {

    @Test
    public void disableEnableAndDeleteStudentOperateByStatusRules() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(post("/admin/students/1/disable"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/admin/students/2/enable"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/admin/students/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void professionalCreateUpdateAndDeleteRespectRelations() throws Exception {
        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(post("/admin/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"physics\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/admin/professionals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"computer science and technology\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/admin/professionals/3"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/admin/professionals/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void listEndpointsReturnExpectedData() throws Exception {
        MockMvc mockMvc = createMockMvc();

        String pending = mockMvc.perform(get("/admin/students/pending"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String professionals = mockMvc.perform(get("/admin/professionals"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(pending.contains("\"success\":true"));
        assertTrue(professionals.contains("\"success\":true"));
    }

    private MockMvc createMockMvc() {
        InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class one", 2022, "company-a", "hangzhou", "13800000000", "student-a@example.com"));
        studentRepository.save(new Student(2L, "student-b", "123456", AuditStatus.DISABLED, 0, null,
                "computer science", "class one", 2022, "company-b", "hangzhou", "13800000001", "student-b@example.com"));
        studentRepository.save(new Student(3L, "student-c", "123456", AuditStatus.PENDING, 0, null,
                "mathematics", "class two", 2023, "company-c", "beijing", "13800000002", "student-c@example.com"));
        studentRepository.save(new Student(4L, "student-d", "123456", AuditStatus.APPROVED, 0, null,
                "mathematics", "class three", 2024, "company-d", "shenzhen", "13800000003", "student-d@example.com"));
        InMemoryProfessionalRepository professionalRepository = new InMemoryProfessionalRepository();
        professionalRepository.save(new Professional(1L, "computer science"));
        professionalRepository.save(new Professional(2L, "software engineering"));
        professionalRepository.save(new Professional(3L, "mathematics"));
        AdminStudentService adminStudentService = new AdminStudentService(studentRepository);
        ProfessionalService professionalService = new ProfessionalService(professionalRepository, studentRepository);
        return MockMvcBuilders.standaloneSetup(new AdminController(adminStudentService, professionalService)).build();
    }
}
