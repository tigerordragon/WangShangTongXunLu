package com.addressbook.student;

import com.addressbook.auth.AuthTokenPair;
import com.addressbook.auth.AuthTokenService;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

/** 负责学生登录校验和登录信息维护。 */
public class StudentLoginService {
    private final StudentRepository studentRepository;
    private final AuthTokenService tokenService;
    private final Clock clock;

    /** 创建学生登录服务。 */
    public StudentLoginService(StudentRepository studentRepository, AuthTokenService tokenService, Clock clock) {
        this.studentRepository = studentRepository;
        this.tokenService = tokenService;
        this.clock = clock;
    }

    /** 校验学生登录并签发双 token。 */
    public LoginResult login(String username, String password) {
        Optional<Student> optionalStudent = studentRepository.findByUsername(username);
        if (!optionalStudent.isPresent() || !optionalStudent.get().passwordMatches(password)) {
            return LoginResult.failure(LoginStatus.INVALID_CREDENTIALS);
        }
        Student student = optionalStudent.get();
        if (student.getAuditStatus() != AuditStatus.APPROVED) {
            return LoginResult.failure(LoginStatus.NOT_APPROVED);
        }
        Student loggedInStudent = student.recordLogin(Instant.now(clock));
        studentRepository.save(loggedInStudent);
        AuthTokenPair tokenPair = tokenService.issueTokenPair(student.getId());
        return LoginResult.success(tokenPair, loggedInStudent.getLoginCount(), loggedInStudent.getLastLoginTime());
    }

    /** 查询学生登录次数和最近登录时间。 */
    public Optional<LoginInfo> getLoginInfo(Long studentId) {
        return studentRepository.findById(studentId)
                .map(student -> new LoginInfo(student.getLoginCount(), student.getLastLoginTime()));
    }
}
