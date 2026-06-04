package com.addressbook.repository.mybatis;

import com.addressbook.entity.AdminUser;
import com.addressbook.entity.AuditStatus;
import com.addressbook.mapper.AdminUserMapper;
import com.addressbook.repository.AdminUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/** 基于 MyBatis 的管理员仓储实现。 */
@Repository
public class MyBatisAdminUserRepository implements AdminUserRepository {
    private final AdminUserMapper adminUserMapper;

    public MyBatisAdminUserRepository(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public void save(AdminUser adminUser) {
        if (adminUserMapper.update(adminUser) == 0) {
            adminUserMapper.insert(adminUser);
        }
    }

    @Override
    public Optional<AdminUser> findByUsername(String username) {
        return Optional.ofNullable(adminUserMapper.selectByUsername(username));
    }

    @Override
    public Optional<AdminUser> findById(Long id) {
        return Optional.ofNullable(adminUserMapper.selectById(id));
    }

    @Override
    public Collection<AdminUser> findByAuditStatus(AuditStatus auditStatus) {
        return adminUserMapper.selectByAuditStatus(auditStatus);
    }

    @Override
    public Long nextId() {
        return adminUserMapper.selectMaxId() + 1L;
    }
}
