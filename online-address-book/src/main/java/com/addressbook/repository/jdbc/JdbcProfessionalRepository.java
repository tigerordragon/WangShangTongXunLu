package com.addressbook.repository.jdbc;

import com.addressbook.entity.Professional;
import com.addressbook.repository.ProfessionalRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** 使用 MySQL 持久化专业数据，表结构见 sql/schema.sql。 */
@Repository
public class JdbcProfessionalRepository implements ProfessionalRepository {
    private static final RowMapper<Professional> ROW_MAPPER = (rs, rowNum) ->
            new Professional(rs.getLong("id"), rs.getString("name"));

    private final JdbcTemplate jdbcTemplate;

    /** 创建 JDBC 专业仓储。 */
    public JdbcProfessionalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Professional professional) {
        int updated = jdbcTemplate.update(
                "UPDATE professional SET name = ? WHERE id = ?",
                professional.getName(),
                professional.getId()
        );
        if (updated == 0) {
            jdbcTemplate.update(
                    "INSERT INTO professional (id, name) VALUES (?, ?)",
                    professional.getId(),
                    professional.getName()
            );
        }
    }

    @Override
    public Optional<Professional> findById(Long id) {
        List<Professional> rows = jdbcTemplate.query(
                "SELECT id, name FROM professional WHERE id = ?",
                ROW_MAPPER,
                id
        );
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    @Override
    public Optional<Professional> findByName(String name) {
        List<Professional> rows = jdbcTemplate.query(
                "SELECT id, name FROM professional WHERE name = ?",
                ROW_MAPPER,
                name
        );
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    @Override
    public Collection<Professional> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name FROM professional ORDER BY id",
                ROW_MAPPER
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM professional WHERE id = ?", id);
    }

    @Override
    public Long nextId() {
        Long maxId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(id), 0) FROM professional",
                Long.class
        );
        return maxId + 1L;
    }
}
