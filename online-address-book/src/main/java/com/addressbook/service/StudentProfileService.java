package com.addressbook.service;

import com.addressbook.dto.StudentProfileResponse;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** 负责学生本人通讯录信息的查询与保存。*/
@Service
public class StudentProfileService {
    private final StudentRepository studentRepository;

    /** 创建本人通讯录服务。*/
    public StudentProfileService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** 查询学生本人通讯录信息。*/
    public Optional<StudentProfileResponse> getProfile(Long studentId) {
        return studentRepository.findById(studentId)
                .map(StudentProfileResponse::from);
    }

    /** 保存学生本人通讯录信息。*/
    public Optional<StudentProfileResponse> saveProfile(Long studentId, String major, String className,
                                                      Integer enrollmentYear, String jobUnit, String city,
                                                      String contactMethod, String email) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            return Optional.empty();
        }
        Student student = optionalStudent.get();
        Student updated = student.updateContact(
                trimToNull(major),
                trimToNull(className),
                enrollmentYear,
                trimToNull(jobUnit),
                trimToNull(city),
                trimToNull(contactMethod),
                trimToNull(email)
        );
        studentRepository.save(updated);
        return Optional.of(StudentProfileResponse.from(updated));
    }

    /** 将空白字符串转为 null。*/
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
