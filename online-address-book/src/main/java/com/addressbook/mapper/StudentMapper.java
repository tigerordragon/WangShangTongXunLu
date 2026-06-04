package com.addressbook.mapper;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 学生表 MyBatis Mapper。 */
public interface StudentMapper {
    int insert(Student student);

    int update(Student student);

    Student selectById(@Param("id") Long id);

    Student selectByUsername(@Param("username") String username);

    List<Student> selectAll();

    List<Student> selectByAuditStatus(@Param("auditStatus") AuditStatus auditStatus);

    int deleteById(@Param("id") Long id);
}
