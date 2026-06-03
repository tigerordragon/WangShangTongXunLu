package com.addressbook.service;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StudentRegistrationServiceTest {

    @Test
    public void registerCreatesPendingStudentWithNextId() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "existing", "123456", AuditStatus.APPROVED, 1, null));
        StudentRegistrationService service = new StudentRegistrationService(repository);

        StudentRegistrationResult result = service.register("student-new", "abc123");

        assertEquals(StudentRegistrationStatus.SUCCESS, result.getStatus());
        assertEquals(Long.valueOf(3L), result.getStudentId());
        assertEquals(AuditStatus.PENDING, result.getAuditStatus());
        assertTrue(repository.findByUsername("student-new").isPresent());
        assertEquals(AuditStatus.PENDING, repository.findByUsername("student-new").get().getAuditStatus());
    }

    @Test
    public void registerRejectsDuplicateUsername() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "student-dup", "123456", AuditStatus.APPROVED, 0, null));
        StudentRegistrationService service = new StudentRegistrationService(repository);

        StudentRegistrationResult result = service.register("student-dup", "abc123");

        assertEquals(StudentRegistrationStatus.DUPLICATE_USERNAME, result.getStatus());
        assertNull(result.getStudentId());
    }

    @Test
    public void registerRejectsBlankInput() {
        StudentRegistrationService service = new StudentRegistrationService(new InMemoryStudentRepository());

        StudentRegistrationResult result = service.register(" ", "");

        assertEquals(StudentRegistrationStatus.INVALID_INPUT, result.getStatus());
        assertNull(result.getStudentId());
    }
}
