package com.addressbook.web;

import com.addressbook.auth.AuthTokenService;
import com.addressbook.auth.InMemoryRefreshTokenStore;
import com.addressbook.auth.RefreshTokenStore;
import com.addressbook.student.AuditStatus;
import com.addressbook.student.InMemoryStudentRepository;
import com.addressbook.student.Student;
import com.addressbook.student.StudentLoginService;
import com.addressbook.student.StudentRepository;

import java.time.Clock;

/** 创建并持有当前 Web 应用需要的服务对象。 */
public class ApplicationContext {
    private static final ApplicationContext INSTANCE = new ApplicationContext();

    private final StudentAuthController studentAuthController;

    /** 初始化演示数据和登录服务。 */
    private ApplicationContext() {
        Clock clock = Clock.systemDefaultZone();
        RefreshTokenStore refreshTokenStore = new InMemoryRefreshTokenStore();
        AuthTokenService authTokenService = new AuthTokenService("online-address-book-secret", refreshTokenStore, clock);
        StudentRepository studentRepository = new InMemoryStudentRepository();
        studentRepository.save(new Student(1L, "student", "123456", AuditStatus.APPROVED, 0, null));
        StudentLoginService studentLoginService = new StudentLoginService(studentRepository, authTokenService, clock);
        this.studentAuthController = new StudentAuthController(studentLoginService, authTokenService);
    }

    /** 返回应用上下文单例。 */
    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    /** 返回学生认证控制器。 */
    public StudentAuthController getStudentAuthController() {
        return studentAuthController;
    }
}
