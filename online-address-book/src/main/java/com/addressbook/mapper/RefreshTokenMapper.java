package com.addressbook.mapper;

import com.addressbook.entity.RefreshTokenRecord;
import org.apache.ibatis.annotations.Param;

/** 刷新令牌表 MyBatis Mapper。 */
public interface RefreshTokenMapper {
    int insert(RefreshTokenRecord record);

    int update(RefreshTokenRecord record);

    RefreshTokenRecord selectByToken(@Param("token") String token);

    int revoke(@Param("token") String token);
}
