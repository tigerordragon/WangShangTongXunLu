package com.addressbook.repository;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** 在内存中保存学生数据，供当前登录模块使用。*/
@Repository
public class InMemoryStudentRepository implements StudentRepository {
    private final Map<Long, Student> studentsById = new ConcurrentHashMap<Long, Student>();
    private final Map<String, Student> studentsByUsername = new ConcurrentHashMap<String, Student>();

    /** 初始化演示学生账号。*/
    public InMemoryStudentRepository() {
        save(new Student(1L, "student", "123456", AuditStatus.APPROVED, 0, null));
    }

    /** 保存学生记录。*/
    @Override
    public void save(Student student) {
        studentsById.put(student.getId(), student);
        studentsByUsername.put(student.getUsername(), student);
    }

    /** 按用户名查询学生。*/
    @Override
    public Optional<Student> findByUsername(String username) {
        return Optional.ofNullable(studentsByUsername.get(username));
    }

    /** 按学生 ID 查询学生。*/
    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(studentsById.get(id));
    }

    /** 返回全部学生。*/
    @Override
    public Collection<Student> findAll() {
        return studentsById.values();
    }
}
