package com.addressbook.service;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdminStudentServiceTest {

    @Test
    public void disableApprovedAccountMovesStudentToDisabledState() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 0, null));
        AdminStudentService service = new AdminStudentService(repository);

        boolean result = service.disableApprovedAccount(1L);

        assertTrue(result);
        assertEquals(AuditStatus.DISABLED, repository.findById(1L).get().getAuditStatus());
    }

    @Test
    public void enableDisabledAccountRestoresApprovedState() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "student-b", "123456", AuditStatus.DISABLED, 0, null));
        AdminStudentService service = new AdminStudentService(repository);

        boolean result = service.enableDisabledAccount(2L);

        assertTrue(result);
        assertEquals(AuditStatus.APPROVED, repository.findById(2L).get().getAuditStatus());
    }

    @Test
    public void deleteUnapprovedAccountDeletesPendingStudent() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(3L, "student-c", "123456", AuditStatus.PENDING, 0, null));
        AdminStudentService service = new AdminStudentService(repository);

        boolean result = service.deleteUnapprovedAccount(3L);

        assertTrue(result);
        assertFalse(repository.findById(3L).isPresent());
    }

    @Test
    public void deleteUnapprovedAccountRejectsApprovedStudent() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(4L, "student-d", "123456", AuditStatus.APPROVED, 0, null));
        AdminStudentService service = new AdminStudentService(repository);

        boolean result = service.deleteUnapprovedAccount(4L);

        assertFalse(result);
        assertTrue(repository.findById(4L).isPresent());
    }

    @Test
    public void listDisabledAccountsReturnsOnlyDisabledStudents() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(5L, "student-e", "123456", AuditStatus.DISABLED, 0, null));
        repository.save(new Student(6L, "student-f", "123456", AuditStatus.APPROVED, 0, null));
        AdminStudentService service = new AdminStudentService(repository);

        List<?> students = service.listDisabledAccounts();

        assertEquals(1, students.size());
    }
}
