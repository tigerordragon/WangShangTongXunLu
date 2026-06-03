package com.addressbook.service;

import com.addressbook.dto.StudentContactResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StudentContactServiceTest {

    @Test
    public void searchReturnsOnlyApprovedOtherStudentsMatchingAllConditions() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(1L, "current", "123456", AuditStatus.APPROVED, 0, null,
                "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "a@example.com"));
        repository.save(new Student(2L, "approved-match", "123456", AuditStatus.APPROVED, 0, null,
                "计算机科学", "一班", 2022, "B公司", "上海", "13811111111", "b@example.com"));
        repository.save(new Student(3L, "approved-different", "123456", AuditStatus.APPROVED, 0, null,
                "软件工程", "一班", 2022, "C公司", "北京", "13822222222", "c@example.com"));
        repository.save(new Student(4L, "pending-match", "123456", AuditStatus.PENDING, 0, null,
                "计算机科学", "一班", 2022, "D公司", "深圳", "13833333333", "d@example.com"));

        StudentContactService service = new StudentContactService(repository);
        List<StudentContactResponse> contacts = service.search(1L, "计算机科学", "一班", 2022);

        assertEquals(1, contacts.size());
        assertEquals("approved-match", contacts.get(0).getUsername());
        assertEquals("B公司", contacts.get(0).getJobUnit());
        assertFalse(contacts.get(0).getUsername().equals("current"));
    }

    @Test
    public void searchTreatsBlankConditionsAsWildcard() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(10L, "current", "123456", AuditStatus.APPROVED, 0, Instant.now(),
                "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "a@example.com"));
        repository.save(new Student(11L, "approved-one", "123456", AuditStatus.APPROVED, 0, null,
                "计算机科学", "二班", 2023, "B公司", "上海", "13811111111", "b@example.com"));
        repository.save(new Student(12L, "approved-two", "123456", AuditStatus.APPROVED, 0, null,
                "软件工程", "三班", 2021, "C公司", "北京", "13822222222", "c@example.com"));

        StudentContactService service = new StudentContactService(repository);
        List<StudentContactResponse> contacts = service.search(10L, " ", "", null);

        assertEquals(3, contacts.size());
    }
}
