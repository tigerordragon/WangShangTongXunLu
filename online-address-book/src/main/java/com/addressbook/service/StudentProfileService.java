package com.addressbook.service;

import com.addressbook.dto.StudentProfileRequest;
import com.addressbook.dto.StudentProfileResponse;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** 负责学生本人通讯录信息的查询与保存。*/
@Service
public class StudentProfileService {
    private static final int MIN_ENROLLMENT_YEAR = 1900;
    private static final int MAX_ENROLLMENT_YEAR = 2100;

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
    public ProfileSaveResult saveProfile(Long studentId, StudentProfileRequest request) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (!optionalStudent.isPresent()) {
            return ProfileSaveResult.failure(ProfileSaveStatus.STUDENT_NOT_FOUND, "学生不存在");
        }
        if (request == null) {
            return ProfileSaveResult.failure(ProfileSaveStatus.INVALID_REQUEST, "请求体不能为空");
        }
        Integer enrollmentYear = request.getEnrollmentYear();
        if (enrollmentYear != null && !isValidEnrollmentYear(enrollmentYear)) {
            return ProfileSaveResult.failure(ProfileSaveStatus.INVALID_ENROLLMENT_YEAR,
                    "入学年份需在 " + MIN_ENROLLMENT_YEAR + " 到 " + MAX_ENROLLMENT_YEAR + " 之间");
        }
        String email = trimToNull(request.getEmail());
        if (email != null && !isValidEmail(email)) {
            return ProfileSaveResult.failure(ProfileSaveStatus.INVALID_EMAIL, "邮箱格式不正确");
        }
        Student student = optionalStudent.get();
        Student updated = student.updateContact(
                trimToNull(request.getMajor()),
                trimToNull(request.getClassName()),
                enrollmentYear,
                trimToNull(request.getJobUnit()),
                trimToNull(request.getCity()),
                trimToNull(request.getContactMethod()),
                email
        );
        studentRepository.save(updated);
        return ProfileSaveResult.success(StudentProfileResponse.from(updated));
    }

    /** 判断入学年份是否在允许范围内。*/
    private boolean isValidEnrollmentYear(int enrollmentYear) {
        return enrollmentYear >= MIN_ENROLLMENT_YEAR && enrollmentYear <= MAX_ENROLLMENT_YEAR;
    }

    /** 判断邮箱格式是否有效。*/
    private boolean isValidEmail(String email) {
        int at = email.indexOf('@');
        int dot = email.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < email.length() - 1;
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
