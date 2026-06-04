package com.addressbook.repository;

import com.addressbook.entity.AdminUser;
import com.addressbook.entity.AuditStatus;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/** 内存管理员仓储，供单元测试使用。 */
public class InMemoryAdminUserRepository implements AdminUserRepository {
    private final Map<Long, AdminUser> byId = new ConcurrentHashMap<Long, AdminUser>();
    private final Map<String, AdminUser> byUsername = new ConcurrentHashMap<String, AdminUser>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    public InMemoryAdminUserRepository() {
        Instant seedTime = Instant.parse("2025-08-01T00:00:00Z");
        save(new AdminUser(1L, "gl1", "123456", "Default Admin", AuditStatus.APPROVED, "system", seedTime, seedTime));
        idGenerator.set(2L);
    }

    @Override
    public void save(AdminUser adminUser) {
        AdminUser existing = byId.get(adminUser.getId());
        if (existing != null && !existing.getUsername().equals(adminUser.getUsername())) {
            byUsername.remove(existing.getUsername());
        }
        byId.put(adminUser.getId(), adminUser);
        byUsername.put(adminUser.getUsername(), adminUser);
        if (adminUser.getId() >= idGenerator.get()) {
            idGenerator.set(adminUser.getId() + 1L);
        }
    }

    @Override
    public Optional<AdminUser> findByUsername(String username) {
        return Optional.ofNullable(byUsername.get(username));
    }

    @Override
    public Optional<AdminUser> findById(Long id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Collection<AdminUser> findByAuditStatus(AuditStatus auditStatus) {
        return byId.values().stream()
                .filter(user -> user.getAuditStatus() == auditStatus)
                .sorted((left, right) -> Long.compare(left.getId(), right.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Long nextId() {
        return idGenerator.getAndIncrement();
    }
}
