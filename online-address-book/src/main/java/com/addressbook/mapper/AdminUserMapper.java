package com.addressbook.mapper;

import com.addressbook.entity.AdminUser;
import com.addressbook.entity.AuditStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 管理员表 MyBatis Mapper。 */
public interface AdminUserMapper {
    int insert(AdminUser adminUser);

    int update(AdminUser adminUser);

    AdminUser selectById(@Param("id") Long id);

    AdminUser selectByUsername(@Param("username") String username);

    List<AdminUser> selectByAuditStatus(@Param("auditStatus") AuditStatus auditStatus);

    Long selectMaxId();
}
