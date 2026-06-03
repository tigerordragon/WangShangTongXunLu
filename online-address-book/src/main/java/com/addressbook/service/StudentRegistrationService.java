package com.addressbook.service;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/** 负责学生注册与待审状态初始化。 */
@Service
public class StudentRegistrationService {
    private final StudentRepository studentRepository;

    /** 创建学生注册服务。 */
    public StudentRegistrationService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** 提交学生注册信息。 */
    public StudentRegistrationResult register(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return StudentRegistrationResult.failure(StudentRegistrationStatus.INVALID_INPUT);
        }
        Optional<Student> existingStudent = studentRepository.findByUsername(username);
        if (existingStudent.isPresent()) {
            return StudentRegistrationResult.failure(StudentRegistrationStatus.DUPLICATE_USERNAME);
        }
        Long studentId = nextStudentId(studentRepository.findAll());
        studentRepository.save(new Student(studentId, username, password, AuditStatus.PENDING, 0, null));
        return StudentRegistrationResult.success(studentId);
    }

    /** 判断字符串是否为空。 */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** 生成下一个学生 ID。 */
    private Long nextStudentId(Collection<Student> students) {
        Long maxId = 0L;
        for (Student student : students) {
            if (student.getId() != null && student.getId() > maxId) {
                maxId = student.getId();
            }
        }
        return maxId + 1L;
    }
}
