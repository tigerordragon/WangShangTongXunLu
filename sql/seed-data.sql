-- =============================================================================
-- 网上通讯录 - 测试数据重灌脚本（需已执行 schema.sql 建表）
-- 用法: mysql --default-character-set=utf8mb4 -u root -p online_address_book < sql/seed-data.sql
-- =============================================================================

SET NAMES utf8mb4;

USE online_address_book;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE refresh_token;
TRUNCATE TABLE student;
TRUNCATE TABLE professional;
TRUNCATE TABLE admin_user;
SET FOREIGN_KEY_CHECKS = 1;

-- 专业
INSERT INTO professional (id, name, created_at, updated_at) VALUES
(1, '计算机科学与技术', '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000'),
(2, '软件工程',           '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000'),
(3, '信息管理与信息系统', '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000'),
(4, '物联网工程',         '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000');

-- 学生
INSERT INTO student (
    id, username, password, audit_status, login_count, last_login_time,
    major, class_name, enrollment_year, job_unit, city, contact_method, email,
    created_at, updated_at
) VALUES
(1, 'student', '123456', 'APPROVED', 12, '2026-06-02 14:30:00.000',
 '计算机科学与技术', '计科2201班', 2022, '某科技有限公司', '北京', '13800001001', 'student@example.com',
 '2025-09-10 08:00:00.000', '2026-06-02 14:30:00.000'),
(2, 'zhangsan', '123456', 'APPROVED', 5, '2026-06-01 10:15:00.000',
 '计算机科学与技术', '计科2201班', 2022, '字节跳动', '上海', '13800001002', 'zhangsan@example.com',
 '2025-09-15 09:20:00.000', '2026-06-01 10:15:00.000'),
(3, 'lisi', '123456', 'APPROVED', 3, '2026-05-28 16:40:00.000',
 '计算机科学与技术', '计科2202班', 2022, '华为技术有限公司', '深圳', '13800001003', 'lisi@example.com',
 '2025-09-18 11:00:00.000', '2026-05-28 16:40:00.000'),
(4, 'wangwu', '123456', 'APPROVED', 8, '2026-06-03 09:05:00.000',
 '软件工程', '软工2101班', 2021, '腾讯科技', '广州', '13800001004', 'wangwu@example.com',
 '2025-10-01 14:30:00.000', '2026-06-03 09:05:00.000'),
(5, 'zhaoliu', '123456', 'APPROVED', 2, '2026-05-20 18:00:00.000',
 '信息管理与信息系统', '信管2201班', 2022, '阿里巴巴', '杭州', '13800001005', 'zhaoliu@example.com',
 '2025-10-08 10:00:00.000', '2026-05-20 18:00:00.000'),
(6, 'sunqi_pending', '123456', 'PENDING', 0, NULL,
 NULL, NULL, NULL, NULL, NULL, NULL, NULL,
 '2026-06-01 08:30:00.000', '2026-06-01 08:30:00.000'),
(7, 'wuqi_pending', '123456', 'PENDING', 0, NULL,
 NULL, NULL, NULL, NULL, NULL, NULL, NULL,
 '2026-06-02 19:00:00.000', '2026-06-02 19:00:00.000'),
(8, 'zhengshi_reject', '123456', 'REJECTED', 0, NULL,
 NULL, NULL, NULL, NULL, NULL, NULL, NULL,
 '2026-05-15 12:00:00.000', '2026-05-16 09:00:00.000'),
(9, 'wangjiu_reject', '123456', 'REJECTED', 0, NULL,
 '软件工程', '软工2203班', 2022, NULL, NULL, NULL, NULL,
 '2026-05-20 13:00:00.000', '2026-05-21 10:00:00.000'),
(10, 'qianba_disabled', '123456', 'DISABLED', 6, '2026-04-10 11:20:00.000',
 '计算机科学与技术', '计科2201班', 2022, '原单位已离职', '成都', '13800001010', 'qianba@example.com',
 '2025-11-01 09:00:00.000', '2026-05-01 15:00:00.000');

-- 刷新令牌
INSERT INTO refresh_token (token, student_id, expires_at, active, created_at) VALUES
('a1b2c3d4e5f6789012345678abcdef01', 1, '2026-06-10 14:30:00.000', 1, '2026-06-03 14:30:00.000'),
('b2c3d4e5f6789012345678abcdef0123', 2, '2026-06-09 10:15:00.000', 1, '2026-06-02 10:15:00.000'),
('c3d4e5f6789012345678abcdef012345', 1, '2026-05-01 00:00:00.000', 0, '2026-04-24 00:00:00.000'),
('d4e5f6789012345678abcdef01234567', 4, '2026-05-20 00:00:00.000', 1, '2026-05-13 09:05:00.000');

-- 管理员
INSERT INTO admin_user (
    id, username, password, display_name, audit_status, reviewed_by, reviewed_at, created_at, updated_at
) VALUES
(1, 'gl1',    '123456', '系统管理员',   'APPROVED', 'system', '2025-08-01 00:00:00.000', '2025-08-01 00:00:00.000', '2025-08-01 00:00:00.000'),
(2, 'admin2', '123456', '教务管理员',   'APPROVED', 'gl1',    '2025-10-01 10:00:00.000', '2025-09-20 14:00:00.000', '2025-10-01 10:00:00.000'),
(3, 'admin3', '123456', '待审管理员',   'PENDING',  NULL,     NULL,                      '2026-06-01 16:00:00.000', '2026-06-01 16:00:00.000'),
(4, 'admin4', '123456', '被拒绝管理员', 'REJECTED', 'gl1',    '2026-05-10 11:00:00.000', '2026-05-09 09:00:00.000', '2026-05-10 11:00:00.000');

ALTER TABLE professional AUTO_INCREMENT = 5;
ALTER TABLE student AUTO_INCREMENT = 11;
ALTER TABLE admin_user AUTO_INCREMENT = 5;
