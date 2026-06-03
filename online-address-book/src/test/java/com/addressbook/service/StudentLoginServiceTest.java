package com.addressbook.service;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.InMemoryRefreshTokenStore;
import com.addressbook.repository.InMemoryStudentRepository;
import com.addressbook.service.auth.AuthTokenService;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StudentLoginServiceTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-06-02T11:00:00Z"), ZoneOffset.UTC);

    @Test
    public void loginSucceedsForApprovedStudentAndUpdatesLoginInfo() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(1L, "student-a", "123456", AuditStatus.APPROVED, 2, null));
        StudentLoginService service = createService(repository);

        LoginResult result = service.login("student-a", "123456");

        assertEquals(LoginStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getTokenPair());
        assertEquals(3, result.getLoginCount());
        assertEquals(Instant.parse("2026-06-02T11:00:00Z"), result.getLastLoginTime());
        assertEquals(3, repository.findByUsername("student-a").get().getLoginCount());
    }

    @Test
    public void loginFailsForPendingStudent() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(2L, "student-b", "123456", AuditStatus.PENDING, 0, null));
        StudentLoginService service = createService(repository);

        LoginResult result = service.login("student-b", "123456");

        assertEquals(LoginStatus.NOT_APPROVED, result.getStatus());
        assertFalse(result.hasTokenPair());
    }

    @Test
    public void loginFailsForDisabledStudent() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(5L, "student-e", "123456", AuditStatus.DISABLED, 0, null));
        StudentLoginService service = createService(repository);

        LoginResult result = service.login("student-e", "123456");

        assertEquals(LoginStatus.DISABLED, result.getStatus());
        assertFalse(result.hasTokenPair());
    }

    @Test
    public void loginFailsForWrongPassword() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(3L, "student-c", "123456", AuditStatus.APPROVED, 0, null));
        StudentLoginService service = createService(repository);

        LoginResult result = service.login("student-c", "wrong");

        assertEquals(LoginStatus.INVALID_CREDENTIALS, result.getStatus());
        assertFalse(result.hasTokenPair());
    }

    @Test
    public void loginInfoReturnsCurrentStudentRecord() {
        InMemoryStudentRepository repository = new InMemoryStudentRepository();
        repository.save(new Student(4L, "student-d", "123456", AuditStatus.APPROVED, 5, Instant.parse("2026-06-01T11:00:00Z")));
        StudentLoginService service = createService(repository);

        LoginInfo info = service.getLoginInfo(4L).get();

        assertEquals(5, info.getLoginCount());
        assertEquals(Instant.parse("2026-06-01T11:00:00Z"), info.getLastLoginTime());
    }

    private StudentLoginService createService(InMemoryStudentRepository repository) {
        AuthTokenService tokenService = new AuthTokenService("test-secret", new InMemoryRefreshTokenStore(), clock);
        return new StudentLoginService(repository, tokenService, clock);
    }
}
