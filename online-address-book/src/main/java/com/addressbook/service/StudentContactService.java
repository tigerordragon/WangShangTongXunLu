package com.addressbook.service;

import com.addressbook.dto.StudentContactResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** 负责查询可公开的同学通讯录信息。*/
@Service
public class StudentContactService {
    private final StudentRepository studentRepository;

    /** 创建同学通讯录查询服务。*/
    public StudentContactService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** 按条件查询同学通讯录。*/
    public List<StudentContactResponse> search(Long currentStudentId, String major, String className, Integer enrollmentYear) {
        Collection<Student> students = studentRepository.findAll();
        return students.stream()
                .filter(student -> student.getAuditStatus() == AuditStatus.APPROVED)
                .filter(student -> !Objects.equals(student.getId(), currentStudentId))
                .filter(student -> matchesMajor(student, major))
                .filter(student -> matchesClassName(student, className))
                .filter(student -> matchesEnrollmentYear(student, enrollmentYear))
                .map(StudentContactResponse::from)
                .collect(Collectors.toList());
    }

    /** 判断专业是否匹配。*/
    private boolean matchesMajor(Student student, String major) {
        return major == null || major.trim().isEmpty() || major.equals(student.getMajor());
    }

    /** 判断班级是否匹配。*/
    private boolean matchesClassName(Student student, String className) {
        return className == null || className.trim().isEmpty() || className.equals(student.getClassName());
    }

    /** 判断入学年份是否匹配。*/
    private boolean matchesEnrollmentYear(Student student, Integer enrollmentYear) {
        return enrollmentYear == null || enrollmentYear.equals(student.getEnrollmentYear());
    }
}
