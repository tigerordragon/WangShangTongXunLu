# MySQL 接入源码变更记录

## 新增文件

| 路径 | 说明 |
|------|------|
| `online-address-book/src/main/java/com/addressbook/config/DatabaseConfig.java` | HikariCP 数据源与 JdbcTemplate |
| `online-address-book/src/main/java/com/addressbook/repository/jdbc/JdbcStudentRepository.java` | 学生表 JDBC 仓储 |
| `online-address-book/src/main/java/com/addressbook/repository/jdbc/JdbcProfessionalRepository.java` | 专业表 JDBC 仓储 |
| `online-address-book/src/main/java/com/addressbook/repository/jdbc/JdbcRefreshTokenStore.java` | 刷新令牌表 JDBC 仓储 |
| `online-address-book/src/main/resources/jdbc.properties` | 数据库连接配置 |
| `online-address-book/src/main/resources/jdbc.properties.example` | 连接配置示例 |
| `docs/7.MySQL数据库接入.md` | 接入说明 |

## 修改文件

| 路径 | 行号范围 | 变更说明 |
|------|----------|----------|
| `online-address-book/pom.xml` | 约 34–48 | 增加 `spring-jdbc`、`HikariCP`、`mysql-connector-j` 依赖 |
| `online-address-book/src/main/java/com/addressbook/entity/Student.java` | 约 54–58 | 新增 `getPassword()` 供持久化写入 |
| `online-address-book/src/main/java/com/addressbook/entity/Student.java` | 约 69–72 | `withAuditStatus` 保留通讯录字段，避免审核/禁用后数据丢失 |
| `InMemoryStudentRepository.java` | 类注解 | 移除 `@Repository`，仅测试使用 |
| `InMemoryProfessionalRepository.java` | 类注解 | 同上 |
| `InMemoryRefreshTokenStore.java` | 类注解 | 同上 |

## 未删除文件

内存仓储实现保留，单元测试与 Controller 测试继续直接 `new InMemory*()`，不依赖 MySQL。

## 与 SQL 脚本的对应关系

- `student` ↔ `JdbcStudentRepository` / `Student` 实体
- `professional` ↔ `JdbcProfessionalRepository` / `Professional` 实体
- `refresh_token` ↔ `JdbcRefreshTokenStore` / `RefreshTokenRecord` 实体
- `admin_user`：表已建，后端暂未接入（前端仍为 localStorage）
