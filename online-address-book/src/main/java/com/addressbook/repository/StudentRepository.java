package com.addressbook.repository;

import com.addressbook.entity.Student;

import java.util.Collection;
import java.util.Optional;

/** 定义学生数据查询和保存操作。*/
public interface StudentRepository {
    /** 保存学生记录。*/
    void save(Student student);

    /** 按用户名查询学生。*/
    Optional<Student> findByUsername(String username);

    /** 按学生 ID 查询学生。*/
    Optional<Student> findById(Long id);

    /** 返回全部学生。*/
    Collection<Student> findAll();

    /** 删除学生记录。*/
    void deleteById(Long id);
}
