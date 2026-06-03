package com.addressbook.service;

import com.addressbook.dto.StudentProfileRequest;
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
        StudentProfileRequest request = profileRequest(
                " 软件工程 ", " 二班 ", 2023, " B公司 ", " 上海 ", " 13811111111 ", " b@example.com "
        );

        ProfileSaveResult saved = service.saveProfile(2L, request);

        assertEquals(ProfileSaveStatus.SUCCESS, saved.getStatus());
        assertEquals("软件工程", saved.getProfile().getMajor());
        assertEquals("二班", saved.getProfile().getClassName());
        assertEquals(Integer.valueOf(2023), saved.getProfile().getEnrollmentYear());
        assertEquals("B公司", saved.getProfile().getJobUnit());
        assertEquals("上海", saved.getProfile().getCity());
        assertEquals("13811111111", saved.getProfile().getContactMethod());
        assertEquals("b@example.com", saved.getProfile().getEmail());

        Optional<StudentProfileResponse> loaded = service.getProfile(2L);
        assertTrue(loaded.isPresent());
        assertEquals("软件工程", loaded.get().getMajor());
        assertEquals("二班", loaded.get().getClassName());
    }

    @Test
    public void saveProfileReturnsNotFoundWhenStudentMissing() {
        StudentProfileService service = new StudentProfileService(new InMemoryStudentRepository());

        ProfileSaveResult saved = service.saveProfile(99L, profileRequest(
                "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "a@example.com"
        ));

        assertEquals(ProfileSaveStatus.STUDENT_NOT_FOUND, saved.getStatus());
    }

    @Test
    public void saveProfileRejectsInvalidEnrollmentYear() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(3L, "student-c", "123456", AuditStatus.APPROVED, 0, null));
        StudentProfileService service = new StudentProfileService(repository);

        ProfileSaveResult saved = service.saveProfile(3L, profileRequest(
                "计算机科学", "一班", 1800, "A公司", "杭州", "13800000000", "a@example.com"
        ));

        assertEquals(ProfileSaveStatus.INVALID_ENROLLMENT_YEAR, saved.getStatus());
    }

    @Test
    public void saveProfileRejectsInvalidEmail() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(4L, "student-d", "123456", AuditStatus.APPROVED, 0, null));
        StudentProfileService service = new StudentProfileService(repository);

        ProfileSaveResult saved = service.saveProfile(4L, profileRequest(
                "计算机科学", "一班", 2022, "A公司", "杭州", "13800000000", "not-an-email"
        ));

        assertEquals(ProfileSaveStatus.INVALID_EMAIL, saved.getStatus());
    }

    private StudentProfileRequest profileRequest(String major, String className, Integer enrollmentYear,
                                                 String jobUnit, String city, String contactMethod, String email) {
        StudentProfileRequest request = new StudentProfileRequest();
        request.setMajor(major);
        request.setClassName(className);
        request.setEnrollmentYear(enrollmentYear);
        request.setJobUnit(jobUnit);
        request.setCity(city);
        request.setContactMethod(contactMethod);
        request.setEmail(email);
        return request;
    }
}
