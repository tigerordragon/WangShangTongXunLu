package com.addressbook.repository.mybatis;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.mapper.StudentMapper;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/** 基于 MyBatis 的学生仓储实现。 */
@Repository
public class MyBatisStudentRepository implements StudentRepository {
    private final StudentMapper studentMapper;

    public MyBatisStudentRepository(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public void save(Student student) {
        if (studentMapper.update(student) == 0) {
            studentMapper.insert(student);
        }
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        return Optional.ofNullable(studentMapper.selectByUsername(username));
    }

    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(studentMapper.selectById(id));
    }

    @Override
    public void deleteById(Long id) {
        studentMapper.deleteById(id);
    }

    @Override
    public Collection<Student> findAll() {
        return studentMapper.selectAll();
    }

    @Override
    public Collection<Student> findByAuditStatus(AuditStatus auditStatus) {
        return studentMapper.selectByAuditStatus(auditStatus);
    }
}
