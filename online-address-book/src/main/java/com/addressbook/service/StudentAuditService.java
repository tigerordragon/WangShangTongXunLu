package com.addressbook.service;

import com.addressbook.dto.PendingStudentResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** 负责学生审核和待审列表查询。 */
@Service
public class StudentAuditService {
    private final StudentRepository studentRepository;

    /** 创建学生审核服务。 */
    public StudentAuditService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** 查询所有待审核学生。 */
    public List<PendingStudentResponse> listPendingStudents() {
        return studentRepository.findByAuditStatus(AuditStatus.PENDING).stream()
                .map(student -> new PendingStudentResponse(student.getId(), student.getUsername(), student.getAuditStatus().name()))
                .collect(Collectors.toList());
    }

    /** 通过学生审核。 */
    public StudentAuditResult approve(Long studentId) {
        return updateAuditStatus(studentId, AuditStatus.APPROVED);
    }

    /** 拒绝学生审核。 */
    public StudentAuditResult reject(Long studentId) {
        return updateAuditStatus(studentId, AuditStatus.REJECTED);
    }

    /** 更新学生审核状态。 */
    private StudentAuditResult updateAuditStatus(Long studentId, AuditStatus targetStatus) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            return StudentAuditResult.failure(StudentAuditStatus.NOT_FOUND);
        }
        Student student = optionalStudent.get();
        if (student.getAuditStatus() != AuditStatus.PENDING) {
            return StudentAuditResult.failure(StudentAuditStatus.NOT_PENDING);
        }
        studentRepository.save(student.withAuditStatus(targetStatus));
        return StudentAuditResult.success(targetStatus);
    }
}
