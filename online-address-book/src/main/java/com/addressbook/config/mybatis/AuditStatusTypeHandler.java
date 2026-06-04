package com.addressbook.config.mybatis;

import com.addressbook.entity.AuditStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** MyBatis 审核状态与数据库 ENUM 字段互转。 */
@MappedTypes(AuditStatus.class)
public class AuditStatusTypeHandler extends BaseTypeHandler<AuditStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AuditStatus parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public AuditStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : AuditStatus.valueOf(value);
    }

    @Override
    public AuditStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : AuditStatus.valueOf(value);
    }

    @Override
    public AuditStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : AuditStatus.valueOf(value);
    }
}
