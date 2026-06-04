package com.addressbook.repository.jdbc;

import com.addressbook.entity.AuditStatus;
import com.addressbook.entity.Student;
import com.addressbook.repository.StudentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** 使用 MySQL 持久化学生数据，表结构见 sql/schema.sql。 */
@Repository
public class JdbcStudentRepository implements StudentRepository {
    private static final String SELECT_COLUMNS =
            "id, username, password, audit_status, login_count, last_login_time, "
                    + "major, class_name, enrollment_year, job_unit, city, contact_method, email";

    private static final RowMapper<Student> ROW_MAPPER = (rs, rowNum) -> {
        Timestamp lastLogin = rs.getTimestamp("last_login_time");
        Instant lastLoginTime = lastLogin == null ? null : lastLogin.toInstant();
        int enrollmentYearValue = rs.getInt("enrollment_year");
        Integer enrollmentYear = rs.wasNull() ? null : enrollmentYearValue;
        return new Student(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                AuditStatus.valueOf(rs.getString("audit_status")),
                rs.getInt("login_count"),
                lastLoginTime,
                rs.getString("major"),
                rs.getString("class_name"),
                enrollmentYear,
                rs.getString("job_unit"),
                rs.getString("city"),
                rs.getString("contact_method"),
                rs.getString("email")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    /** 创建 JDBC 学生仓储。 */
    public JdbcStudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Student student) {
        int updated = jdbcTemplate.update(
                "UPDATE student SET username=?, password=?, audit_status=?, login_count=?, last_login_time=?, "
                        + "major=?, class_name=?, enrollment_year=?, job_unit=?, city=?, contact_method=?, email=? "
                        + "WHERE id=?",
                student.getUsername(),
                student.getPassword(),
                student.getAuditStatus().name(),
                student.getLoginCount(),
                toTimestamp(student.getLastLoginTime()),
                student.getMajor(),
                student.getClassName(),
                student.getEnrollmentYear(),
                student.getJobUnit(),
                student.getCity(),
                student.getContactMethod(),
                student.getEmail(),
                student.getId()
        );
        if (updated == 0) {
            jdbcTemplate.update(
                    "INSERT INTO student (id, username, password, audit_status, login_count, last_login_time, "
                            + "major, class_name, enrollment_year, job_unit, city, contact_method, email) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    student.getId(),
                    student.getUsername(),
                    student.getPassword(),
                    student.getAuditStatus().name(),
                    student.getLoginCount(),
                    toTimestamp(student.getLastLoginTime()),
                    student.getMajor(),
                    student.getClassName(),
                    student.getEnrollmentYear(),
                    student.getJobUnit(),
                    student.getCity(),
                    student.getContactMethod(),
                    student.getEmail()
            );
        }
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        List<Student> rows = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + " FROM student WHERE username = ?",
                ROW_MAPPER,
                username
        );
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    @Override
    public Optional<Student> findById(Long id) {
        List<Student> rows = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + " FROM student WHERE id = ?",
                ROW_MAPPER,
                id
        );
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM student WHERE id = ?", id);
    }

    @Override
    public Collection<Student> findAll() {
        return jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + " FROM student ORDER BY id",
                ROW_MAPPER
        );
    }

    @Override
    public Collection<Student> findByAuditStatus(AuditStatus auditStatus) {
        return jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + " FROM student WHERE audit_status = ? ORDER BY id",
                ROW_MAPPER,
                auditStatus.name()
        );
    }

    private Timestamp toTimestamp(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
