package com.addressbook.service;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Professional;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryProfessionalRepository;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProfessionalServiceTest {

    @Test
    public void createRejectsDuplicateName() {
        InMemoryProfessionalRepository professionalRepository = new InMemoryProfessionalRepository();
        professionalRepository.save(new Professional(1L, "computer science"));
        ProfessionalService service = new ProfessionalService(professionalRepository, new InMemoryStudentRepository());

        assertFalse(service.create("computer science"));
    }

    @Test
    public void updateRenamesProfessional() {
        InMemoryProfessionalRepository professionalRepository = new InMemoryProfessionalRepository();
        professionalRepository.save(new Professional(1L, "computer science"));
        ProfessionalService service = new ProfessionalService(professionalRepository, new InMemoryStudentRepository());

        assertTrue(service.update(1L, "software engineering"));
        assertEquals("software engineering", professionalRepository.findById(1L).get().getName());
    }

    @Test
    public void deleteRejectsWhenProfessionalIsUsedByStudent() {
        InMemoryProfessionalRepository professionalRepository = new InMemoryProfessionalRepository();
        professionalRepository.save(new Professional(1L, "computer science"));
        InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class one", 2022, "company", "hangzhou", "13800000000", "a@example.com"));
        ProfessionalService service = new ProfessionalService(professionalRepository, studentRepository);

        assertFalse(service.delete(1L));
        assertTrue(professionalRepository.findById(1L).isPresent());
    }

    @Test
    public void listAllReturnsExistingProfessionals() {
        InMemoryProfessionalRepository professionalRepository = new InMemoryProfessionalRepository();
        professionalRepository.save(new Professional(1L, "computer science"));
        professionalRepository.save(new Professional(2L, "software engineering"));
        ProfessionalService service = new ProfessionalService(professionalRepository, new InMemoryStudentRepository());

        List<?> professionals = service.listAll();

        assertEquals(2, professionals.size());
    }
}
