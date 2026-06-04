package com.addressbook.service;

import com.addressbook.entity.AuditStatus;
import com.addressbook.repository.InMemoryAdminUserRepository;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdminAccountServiceTest {

    @Test
    public void registerAndLoginApprovedAdmin() {
        InMemoryAdminUserRepository repository = new InMemoryAdminUserRepository();
        AdminAccountService service = new AdminAccountService(repository, fixedClock());

        AdminRegistrationResult registerResult = service.register("admin-new", "123456", "New Admin");
        assertEquals(AdminRegistrationStatus.SUCCESS, registerResult.getStatus());

        AdminLoginResult pendingLogin = service.login("admin-new", "123456");
        assertEquals(AdminLoginStatus.NOT_APPROVED, pendingLogin.getStatus());

        AdminAuditResult approveResult = service.approve(registerResult.getAdminId(), "gl1");
        assertEquals(AdminAuditStatus.SUCCESS, approveResult.getStatus());

        AdminLoginResult loginResult = service.login("admin-new", "123456");
        assertEquals(AdminLoginStatus.SUCCESS, loginResult.getStatus());
        assertEquals(AuditStatus.APPROVED, loginResult.getAdminUser().getAuditStatus());
    }

    @Test
    public void onlySuperAdminCanReviewAdminRegistration() {
        InMemoryAdminUserRepository repository = new InMemoryAdminUserRepository();
        AdminAccountService service = new AdminAccountService(repository, fixedClock());
        AdminRegistrationResult registerResult = service.register("admin-pending", "123456", "Pending");

        AdminAuditResult forbidden = service.approve(registerResult.getAdminId(), "admin2");
        assertEquals(AdminAuditStatus.FORBIDDEN_REVIEWER, forbidden.getStatus());
        assertTrue(repository.findById(registerResult.getAdminId()).isPresent());
        assertEquals(AuditStatus.PENDING, repository.findById(registerResult.getAdminId()).get().getAuditStatus());
    }

    private Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-06-04T10:00:00Z"), ZoneId.of("UTC"));
    }
}
