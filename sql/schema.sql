-- =============================================================================
-- 网上通讯录系统 - MySQL 8.x 建库建表脚本
-- 依据 docs/2~6 功能说明及 com.addressbook.entity 实体字段设计
-- 当前后端为内存仓储，本脚本用于迁移到 MySQL 持久化
-- =============================================================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS online_address_book
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE online_address_book;

-- -----------------------------------------------------------------------------
-- 2. 学生表（账号 + 审核状态 + 登录记录 + 通讯录信息，对应 Student 实体）
--    docs/2 注册与审核 | docs/3 登录记录 | docs/4 通讯录字段 | docs/5 查询条件
--    docs/6 禁用后不可登录且不出现在查询结果（audit_status = DISABLED）
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS professional;
DROP TABLE IF EXISTS admin_user;

CREATE TABLE student (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '学生主键',
    username        VARCHAR(64)         NOT NULL COMMENT '登录账号，唯一',
    password        VARCHAR(255)        NOT NULL COMMENT '登录密码（建议存 BCrypt 哈希）',
    audit_status    ENUM('PENDING', 'APPROVED', 'REJECTED', 'DISABLED')
                                        NOT NULL DEFAULT 'PENDING' COMMENT '审核/账号状态',
    login_count     INT UNSIGNED        NOT NULL DEFAULT 0 COMMENT '累计登录次数',
    last_login_time DATETIME(3)         NULL COMMENT '最近登录时间',
    major           VARCHAR(100)        NULL COMMENT '专业（与 professional.name 文本匹配，非外键）',
    class_name      VARCHAR(100)        NULL COMMENT '班级',
    enrollment_year SMALLINT UNSIGNED   NULL COMMENT '入学年份',
    job_unit        VARCHAR(200)        NULL COMMENT '就业单位',
    city            VARCHAR(100)        NULL COMMENT '城市',
    contact_method  VARCHAR(100)        NULL COMMENT '联系方式',
    email           VARCHAR(120)        NULL COMMENT '邮箱',
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '注册时间',
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_username (username),
    KEY idx_student_audit_status (audit_status),
    KEY idx_student_search (major, class_name, enrollment_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生账号与通讯录';

-- -----------------------------------------------------------------------------
-- 3. 专业表（对应 Professional 实体，docs/6 专业增删改查）
--    删除专业前需检查是否有 student.major 引用该专业名称
-- -----------------------------------------------------------------------------
CREATE TABLE professional (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '专业主键',
    name        VARCHAR(100)    NOT NULL COMMENT '专业名称，唯一',
    created_at  DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    updated_at  DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_professional_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专业字典';

-- -----------------------------------------------------------------------------
-- 4. 刷新令牌表（对应 RefreshTokenRecord，docs/3 登录与会话）
--    访问令牌为无状态 JWT，不落库；仅持久化 refresh_token
-- -----------------------------------------------------------------------------
CREATE TABLE refresh_token (
    token       VARCHAR(64)     NOT NULL COMMENT '刷新令牌字符串',
    student_id  BIGINT UNSIGNED NOT NULL COMMENT '所属学生',
    expires_at  DATETIME(3)     NOT NULL COMMENT '过期时间',
    active      TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '1=有效 0=已吊销',
    created_at  DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '签发时间',
    PRIMARY KEY (token),
    KEY idx_refresh_token_student (student_id),
    KEY idx_refresh_token_expires (expires_at),
    CONSTRAINT fk_refresh_token_student
        FOREIGN KEY (student_id) REFERENCES student (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生刷新令牌';

-- -----------------------------------------------------------------------------
-- 5. 管理员表（可选，对应前端 localStorage 管理员模型，便于后续接入后端）
--    docs/6 管理员审核；当前 Java 后端未持久化管理员，预留表结构
-- -----------------------------------------------------------------------------
CREATE TABLE admin_user (
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员主键',
    username        VARCHAR(64)     NOT NULL COMMENT '管理员账号',
    password        VARCHAR(255)    NOT NULL COMMENT '密码（建议哈希存储）',
    display_name    VARCHAR(100)    NULL COMMENT '显示名称',
    audit_status    ENUM('PENDING', 'APPROVED', 'REJECTED')
                                    NOT NULL DEFAULT 'PENDING' COMMENT '注册审核状态',
    reviewed_by     VARCHAR(64)     NULL COMMENT '审核人账号',
    reviewed_at     DATETIME(3)     NULL COMMENT '审核时间',
    created_at      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '注册时间',
    updated_at      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_username (username),
    KEY idx_admin_audit_status (audit_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员账号（预留）';

-- -----------------------------------------------------------------------------
-- 6. 测试数据（覆盖各审核状态与业务场景，密码均为 123456 便于联调）
--    更完整的重灌脚本见同目录 seed-data.sql
-- -----------------------------------------------------------------------------

-- 6.1 专业字典（名称唯一；「物联网工程」暂无学生引用，可用于测试删除专业）
INSERT INTO professional (id, name, created_at, updated_at) VALUES
(1, '计算机科学与技术', '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000'),
(2, '软件工程',           '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000'),
(3, '信息管理与信息系统', '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000'),
(4, '物联网工程',         '2025-09-01 09:00:00.000', '2025-09-01 09:00:00.000');

-- 6.2 学生（PENDING / APPROVED / REJECTED / DISABLED 齐全；major 与 professional.name 一致）
INSERT INTO student (
    id, username, password, audit_status, login_count, last_login_time,
    major, class_name, enrollment_year, job_unit, city, contact_method, email,
    created_at, updated_at
) VALUES
-- 默认演示账号，可登录、可查询、可完善通讯录
(1, 'student', '123456', 'APPROVED', 12, '2026-06-02 14:30:00.000',
 '计算机科学与技术', '计科2201班', 2022, '某科技有限公司', '北京', '13800001001', 'student@example.com',
 '2025-09-10 08:00:00.000', '2026-06-02 14:30:00.000'),
-- 同专业同班，用于按条件查询出多条结果
(2, 'zhangsan', '123456', 'APPROVED', 5, '2026-06-01 10:15:00.000',
 '计算机科学与技术', '计科2201班', 2022, '字节跳动', '上海', '13800001002', 'zhangsan@example.com',
 '2025-09-15 09:20:00.000', '2026-06-01 10:15:00.000'),
-- 同专业不同班
(3, 'lisi', '123456', 'APPROVED', 3, '2026-05-28 16:40:00.000',
 '计算机科学与技术', '计科2202班', 2022, '华为技术有限公司', '深圳', '13800001003', 'lisi@example.com',
 '2025-09-18 11:00:00.000', '2026-05-28 16:40:00.000'),
-- 另一专业
(4, 'wangwu', '123456', 'APPROVED', 8, '2026-06-03 09:05:00.000',
 '软件工程', '软工2101班', 2021, '腾讯科技', '广州', '13800001004', 'wangwu@example.com',
 '2025-10-01 14:30:00.000', '2026-06-03 09:05:00.000'),
(5, 'zhaoliu', '123456', 'APPROVED', 2, '2026-05-20 18:00:00.000',
 '信息管理与信息系统', '信管2201班', 2022, '阿里巴巴', '杭州', '13800001005', 'zhaoliu@example.com',
 '2025-10-08 10:00:00.000', '2026-05-20 18:00:00.000'),
-- 待审核：仅账号，无通讯录
(6, 'sunqi_pending', '123456', 'PENDING', 0, NULL,
 NULL, NULL, NULL, NULL, NULL, NULL, NULL,
 '2026-06-01 08:30:00.000', '2026-06-01 08:30:00.000'),
(7, 'wuqi_pending', '123456', 'PENDING', 0, NULL,
 NULL, NULL, NULL, NULL, NULL, NULL, NULL,
 '2026-06-02 19:00:00.000', '2026-06-02 19:00:00.000'),
-- 未通过审核：管理员可删除
(8, 'zhengshi_reject', '123456', 'REJECTED', 0, NULL,
 NULL, NULL, NULL, NULL, NULL, NULL, NULL,
 '2026-05-15 12:00:00.000', '2026-05-16 09:00:00.000'),
(9, 'wangjiu_reject', '123456', 'REJECTED', 0, NULL,
 '软件工程', '软工2203班', 2022, NULL, NULL, NULL, NULL,
 '2026-05-20 13:00:00.000', '2026-05-21 10:00:00.000'),
-- 已禁用：曾有完整通讯录，不可登录且不应出现在同学查询中
(10, 'qianba_disabled', '123456', 'DISABLED', 6, '2026-04-10 11:20:00.000',
 '计算机科学与技术', '计科2201班', 2022, '原单位已离职', '成都', '13800001010', 'qianba@example.com',
 '2025-11-01 09:00:00.000', '2026-05-01 15:00:00.000');

-- 6.3 刷新令牌（仅已通过审核学生；含有效、已吊销、已过期样例）
INSERT INTO refresh_token (token, student_id, expires_at, active, created_at) VALUES
('a1b2c3d4e5f6789012345678abcdef01', 1, '2026-06-10 14:30:00.000', 1, '2026-06-03 14:30:00.000'),
('b2c3d4e5f6789012345678abcdef0123', 2, '2026-06-09 10:15:00.000', 1, '2026-06-02 10:15:00.000'),
('c3d4e5f6789012345678abcdef012345', 1, '2026-05-01 00:00:00.000', 0, '2026-04-24 00:00:00.000'),
('d4e5f6789012345678abcdef01234567', 4, '2026-05-20 00:00:00.000', 1, '2026-05-13 09:05:00.000');

-- 6.4 管理员（gl1 审核其他管理员；含待审、已通过、已拒绝）
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
