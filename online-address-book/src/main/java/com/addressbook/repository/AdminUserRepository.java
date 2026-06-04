package com.addressbook.repository;

import com.addressbook.entity.AdminUser;
import com.addressbook.entity.AuditStatus;

import java.util.Collection;
import java.util.Optional;

/** 定义管理员账号的查询和保存操作。 */
public interface AdminUserRepository {
    /** 保存管理员记录。 */
    void save(AdminUser adminUser);

    /** 按用户名查询。 */
    Optional<AdminUser> findByUsername(String username);

    /** 按 ID 查询。 */
    Optional<AdminUser> findById(Long id);

    /** 按审核状态查询。 */
    Collection<AdminUser> findByAuditStatus(AuditStatus auditStatus);

    /** 生成下一个主键（注册新管理员时使用）。 */
    Long nextId();
}
