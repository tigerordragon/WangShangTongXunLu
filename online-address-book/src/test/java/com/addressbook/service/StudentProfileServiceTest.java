package com.addressbook.service;

import com.addressbook.dto.StudentProfileResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryStudentRepository;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StudentProfileServiceTest {

    @Test
    public void getProfileReturnsSavedContactInfo() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 1, null,
                "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "a@example.com"));
        StudentProfileService service = new StudentProfileService(repository);

        Optional<StudentProfileResponse> profile = service.getProfile(1L);

        assertTrue(profile.isPresent());
        assertEquals("计算机科学", profile.get().getMajor());
        assertEquals("一班", profile.get().getClassName());
        assertEquals(Integer.valueOf(2022), profile.get().getEnrollmentYear());
        assertEquals("A公司", profile.get().getJobUnit());
    }

    @Test
    public void saveProfileUpdatesStudentContactInfo() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "student-b", "123456", AuditStatus.APPROVED, 0, null));
        StudentProfileService service = new StudentProfileService(repository);

        Optional<StudentProfileResponse> saved = service.saveProfile(
                2L, " 软件工程 ", " 二班 ", 2023, " B公司 ", " 上海 ", " 13811111111 ", " b@example.com "
        );

        assertTrue(saved.isPresent());
        assertEquals("软件工程", saved.get().getMajor());
        assertEquals("二班", saved.get().getClassName());
        assertEquals(Integer.valueOf(2023), saved.get().getEnrollmentYear());
        assertEquals("B公司", saved.get().getJobUnit());
        assertEquals("上海", saved.get().getCity());
        assertEquals("13811111111", saved.get().getContactMethod());
        assertEquals("b@example.com", saved.get().getEmail());

        Optional<StudentProfileResponse> loaded = service.getProfile(2L);
        assertTrue(loaded.isPresent());
        assertEquals("软件工程", loaded.get().getMajor());
        assertEquals("二班", loaded.get().getClassName());
    }

    @Test
    public void saveProfileReturnsEmptyWhenStudentMissing() {
        StudentProfileService service = new StudentProfileService(new InMemoryStudentRepository());

        Optional<StudentProfileResponse> saved = service.saveProfile(
                99L, "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "a@example.com"
        );

        assertFalse(saved.isPresent());
    }
}
