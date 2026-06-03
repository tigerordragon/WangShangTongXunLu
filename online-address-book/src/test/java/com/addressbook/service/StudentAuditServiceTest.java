package com.addressbook.service;

import com.addressbook.dto.PendingStudentResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StudentAuditServiceTest {

    @Test
    public void listPendingStudentsReturnsOnlyPendingStudents() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "pending-a", "123456", AuditStatus.PENDING, 0, null));
        repository.save(new Student(3L, "approved-a", "123456", AuditStatus.APPROVED, 0, null));
        repository.save(new Student(4L, "pending-b", "123456", AuditStatus.PENDING, 0, null));
        StudentAuditService service = new StudentAuditService(repository);

        List<PendingStudentResponse> students = service.listPendingStudents();

        assertEquals(2, students.size());
        assertEquals(Long.valueOf(2L), students.get(0).getStudentId());
        assertEquals(Long.valueOf(4L), students.get(1).getStudentId());
    }

    @Test
    public void approveChangesPendingStudentToApproved() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "pending-a", "123456", AuditStatus.PENDING, 0, null));
        StudentAuditService service = new StudentAuditService(repository);

        StudentAuditResult result = service.approve(2L);

        assertEquals(StudentAuditStatus.SUCCESS, result.getStatus());
        assertEquals(AuditStatus.APPROVED, result.getAuditStatus());
        assertEquals(AuditStatus.APPROVED, repository.findById(2L).get().getAuditStatus());
    }

    @Test
    public void rejectFailsForNonPendingStudent() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "approved-a", "123456", AuditStatus.APPROVED, 0, null));
        StudentAuditService service = new StudentAuditService(repository);

        StudentAuditResult result = service.reject(2L);

        assertEquals(StudentAuditStatus.NOT_PENDING, result.getStatus());
        assertNull(result.getAuditStatus());
        assertEquals(AuditStatus.APPROVED, repository.findById(2L).get().getAuditStatus());
    }
}
