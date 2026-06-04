package com.addressbook.repository.jdbc;

import com.addressbook.entity.RefreshTokenRecord;
import com.addressbook.repository.RefreshTokenStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** 使用 MySQL 持久化刷新令牌，表结构见 sql/schema.sql。 */
@Repository
public class JdbcRefreshTokenStore implements RefreshTokenStore {
    private static final RowMapper<RefreshTokenRecord> ROW_MAPPER = (rs, rowNum) ->
            new RefreshTokenRecord(
                    rs.getString("token"),
                    rs.getLong("student_id"),
                    rs.getTimestamp("expires_at").toInstant(),
                    rs.getInt("active") == 1
            );

    private final JdbcTemplate jdbcTemplate;

    /** 创建 JDBC 刷新令牌仓储。 */
    public JdbcRefreshTokenStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(RefreshTokenRecord record) {
        int updated = jdbcTemplate.update(
                "UPDATE refresh_token SET student_id = ?, expires_at = ?, active = ? WHERE token = ?",
                record.getStudentId(),
                Timestamp.from(record.getExpiresAt()),
                record.isActive() ? 1 : 0,
                record.getToken()
        );
        if (updated == 0) {
            jdbcTemplate.update(
                    "INSERT INTO refresh_token (token, student_id, expires_at, active) VALUES (?, ?, ?, ?)",
                    record.getToken(),
                    record.getStudentId(),
                    Timestamp.from(record.getExpiresAt()),
                    record.isActive() ? 1 : 0
            );
        }
    }

    @Override
    public Optional<RefreshTokenRecord> find(String token) {
        List<RefreshTokenRecord> rows = jdbcTemplate.query(
                "SELECT token, student_id, expires_at, active FROM refresh_token WHERE token = ?",
                ROW_MAPPER,
                token
        );
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    @Override
    public void revoke(String token) {
        jdbcTemplate.update("UPDATE refresh_token SET active = 0 WHERE token = ?", token);
    }
}
