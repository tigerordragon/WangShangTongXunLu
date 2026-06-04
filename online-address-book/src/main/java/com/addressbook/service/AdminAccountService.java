package com.addressbook.service;

import com.addressbook.dto.AdminUserResponse;
import com.addressbook.entity.AdminUser;
import com.addressbook.entity.AuditStatus;
import com.addressbook.repository.AdminUserRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** 负责管理员注册、登录与审核。 */
@Service
public class AdminAccountService {
    /** 拥有审核其他管理员权限的超级管理员账号。 */
    public static final String SUPER_ADMIN_USERNAME = "gl1";

    private final AdminUserRepository adminUserRepository;
    private final Clock clock;

    /** 创建管理员账号服务。 */
    public AdminAccountService(AdminUserRepository adminUserRepository, Clock clock) {
        this.adminUserRepository = adminUserRepository;
        this.clock = clock;
    }

    /** 提交管理员注册。 */
    public AdminRegistrationResult register(String username, String password, String displayName) {
        if (isBlank(username) || isBlank(password)) {
            return AdminRegistrationResult.failure(AdminRegistrationStatus.INVALID_INPUT);
        }
        String normalizedUsername = username.trim();
        if (adminUserRepository.findByUsername(normalizedUsername).isPresent()) {
            return AdminRegistrationResult.failure(AdminRegistrationStatus.DUPLICATE_USERNAME);
        }
        String normalizedDisplayName = isBlank(displayName) ? normalizedUsername : displayName.trim();
        Instant now = Instant.now(clock);
        Long id = adminUserRepository.nextId();
        adminUserRepository.save(new AdminUser(
                id,
                normalizedUsername,
                password,
                normalizedDisplayName,
                AuditStatus.PENDING,
                null,
                null,
                now
        ));
        return AdminRegistrationResult.success(id);
    }

    /** 校验管理员登录。 */
    public AdminLoginResult login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return AdminLoginResult.failure(AdminLoginStatus.INVALID_CREDENTIALS);
        }
        Optional<AdminUser> optionalAdmin = adminUserRepository.findByUsername(username.trim());
        if (!optionalAdmin.isPresent() || !optionalAdmin.get().passwordMatches(password)) {
            return AdminLoginResult.failure(AdminLoginStatus.INVALID_CREDENTIALS);
        }
        AdminUser admin = optionalAdmin.get();
        if (admin.getAuditStatus() != AuditStatus.APPROVED) {
            return AdminLoginResult.failure(AdminLoginStatus.NOT_APPROVED);
        }
        return AdminLoginResult.success(admin);
    }

    /** 查询待审核管理员列表。 */
    public List<AdminUserResponse> listPendingAdmins() {
        return adminUserRepository.findByAuditStatus(AuditStatus.PENDING).stream()
                .map(AdminUserResponse::from)
                .collect(Collectors.toList());
    }

    /** 通过管理员注册审核。 */
    public AdminAuditResult approve(Long adminId, String reviewerUsername) {
        return review(adminId, reviewerUsername, true);
    }

    /** 拒绝管理员注册审核。 */
    public AdminAuditResult reject(Long adminId, String reviewerUsername) {
        return review(adminId, reviewerUsername, false);
    }

    private AdminAuditResult review(Long adminId, String reviewerUsername, boolean approve) {
        if (!SUPER_ADMIN_USERNAME.equals(reviewerUsername)) {
            return AdminAuditResult.of(AdminAuditStatus.FORBIDDEN_REVIEWER);
        }
        Optional<AdminUser> optionalAdmin = adminUserRepository.findById(adminId);
        if (!optionalAdmin.isPresent()) {
            return AdminAuditResult.of(AdminAuditStatus.NOT_FOUND);
        }
        AdminUser admin = optionalAdmin.get();
        if (admin.getAuditStatus() != AuditStatus.PENDING) {
            return AdminAuditResult.of(AdminAuditStatus.NOT_PENDING);
        }
        Instant now = Instant.now(clock);
        AdminUser updated = approve
                ? admin.approved(reviewerUsername, now)
                : admin.rejected(reviewerUsername, now);
        adminUserRepository.save(updated);
        return AdminAuditResult.of(AdminAuditStatus.SUCCESS);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
