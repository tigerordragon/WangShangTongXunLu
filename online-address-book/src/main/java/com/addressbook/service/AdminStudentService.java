package com.addressbook.service;

import com.addressbook.dto.AdminStudentResponse;
import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** 负责管理员对学生账号的维护操作。 */
@Service
public class AdminStudentService {
    private final StudentRepository studentRepository;

    /** 创建管理员学生服务。 */
    public AdminStudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /** 查询未通过审核账号，包含待审核和已拒绝。 */
    public List<AdminStudentResponse> listUnapprovedAccounts() {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAuditStatus() == AuditStatus.PENDING || student.getAuditStatus() == AuditStatus.REJECTED)
                .map(AdminStudentResponse::from)
                .collect(Collectors.toList());
    }

    /** 查询所有已通过审核账号。 */
    public List<AdminStudentResponse> listApprovedAccounts() {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAuditStatus() == AuditStatus.APPROVED)
                .map(AdminStudentResponse::from)
                .collect(Collectors.toList());
    }

    /** 查询所有已禁用账号。 */
    public List<AdminStudentResponse> listDisabledAccounts() {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAuditStatus() == AuditStatus.DISABLED)
                .map(AdminStudentResponse::from)
                .collect(Collectors.toList());
    }

    /** 删除未通过审核账号。 */
    public boolean deleteUnapprovedAccount(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return false;
        }
        if (student.getAuditStatus() == AuditStatus.APPROVED || student.getAuditStatus() == AuditStatus.DISABLED) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    /** 禁用已通过审核账号。 */
    public boolean disableApprovedAccount(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null || student.getAuditStatus() != AuditStatus.APPROVED) {
            return false;
        }
        studentRepository.save(student.withAuditStatus(AuditStatus.DISABLED));
        return true;
    }

    /** 启用已禁用账号。 */
    public boolean enableDisabledAccount(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null || student.getAuditStatus() != AuditStatus.DISABLED) {
            return false;
        }
        studentRepository.save(student.withAuditStatus(AuditStatus.APPROVED));
        return true;
    }
}
