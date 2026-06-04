package com.addressbook.repository.mybatis;

import com.addressbook.entity.RefreshTokenRecord;
import com.addressbook.mapper.RefreshTokenMapper;
import com.addressbook.repository.RefreshTokenStore;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** 基于 MyBatis 的刷新令牌仓储实现。 */
@Repository
public class MyBatisRefreshTokenStore implements RefreshTokenStore {
    private final RefreshTokenMapper refreshTokenMapper;

    public MyBatisRefreshTokenStore(RefreshTokenMapper refreshTokenMapper) {
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Override
    public void save(RefreshTokenRecord record) {
        if (refreshTokenMapper.update(record) == 0) {
            refreshTokenMapper.insert(record);
        }
    }

    @Override
    public Optional<RefreshTokenRecord> find(String token) {
        return Optional.ofNullable(refreshTokenMapper.selectByToken(token));
    }

    @Override
    public void revoke(String token) {
        refreshTokenMapper.revoke(token);
    }
}
