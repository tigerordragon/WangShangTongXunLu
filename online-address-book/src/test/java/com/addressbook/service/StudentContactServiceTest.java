package com.addressbook.service;

import com.addressbook.dto.StudentContactResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StudentContactServiceTest {

    @Test
    public void searchReturnsOnlyApprovedOtherStudentsMatchingAllConditions() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(1L, "current", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class one", 2022, "company-a", "hangzhou", "13800000000", "a@example.com"));
        repository.save(new Student(2L, "approved-match", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class one", 2022, "company-b", "shanghai", "13811111111", "b@example.com"));
        repository.save(new Student(3L, "approved-different", "123456", AuditStatus.APPROVED, 0, null,
                "software engineering", "class one", 2022, "company-c", "beijing", "13822222222", "c@example.com"));
        repository.save(new Student(4L, "pending-match", "123456", AuditStatus.PENDING, 0, null,
                "computer science", "class one", 2022, "company-d", "shenzhen", "13833333333", "d@example.com"));
        repository.save(new Student(5L, "disabled-match", "123456", AuditStatus.DISABLED, 0, null,
                "computer science", "class one", 2022, "company-e", "hangzhou", "13844444444", "e@example.com"));

        StudentContactService service = new StudentContactService(repository);
        List<StudentContactResponse> contacts = service.search(1L, "computer science", "class one", 2022);

        assertEquals(1, contacts.size());
        assertEquals("approved-match", contacts.get(0).getUsername());
        assertFalse(contacts.stream().anyMatch(contact -> "current".equals(contact.getUsername())));
        assertFalse(contacts.stream().anyMatch(contact -> "disabled-match".equals(contact.getUsername())));
    }

    @Test
    public void searchTreatsBlankConditionsAsWildcard() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(10L, "current", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class one", 2022, "company-a", "hangzhou", "13800000000", "a@example.com"));
        repository.save(new Student(11L, "approved-one", "123456", AuditStatus.APPROVED, 0, null,
                "computer science", "class two", 2023, "company-b", "shanghai", "13811111111", "b@example.com"));
        repository.save(new Student(12L, "approved-two", "123456", AuditStatus.APPROVED, 0, null,
                "software engineering", "class three", 2021, "company-c", "beijing", "13822222222", "c@example.com"));
        repository.save(new Student(13L, "disabled-one", "123456", AuditStatus.DISABLED, 0, null,
                "computer science", "class four", 2021, "company-d", "shenzhen", "13833333333", "d@example.com"));

        StudentContactService service = new StudentContactService(repository);
        List<StudentContactResponse> contacts = service.search(10L, " ", "", null);

        assertEquals(3, contacts.size());
        assertFalse(contacts.stream().anyMatch(contact -> "disabled-one".equals(contact.getUsername())));
    }
}
