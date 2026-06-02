package com.addressbook.auth;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** 在内存中保存刷新 token，适合当前课程项目的初始实现。 */
public class InMemoryRefreshTokenStore implements RefreshTokenStore {
    private final Map<String, RefreshTokenRecord> tokens = new ConcurrentHashMap<String, RefreshTokenRecord>();

    /** 保存刷新 token 记录。 */
    @Override
    public void save(RefreshTokenRecord record) {
        tokens.put(record.getToken(), record);
    }

    /** 按 token 字符串查询刷新 token 记录。 */
    @Override
    public Optional<RefreshTokenRecord> find(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    /** 标记刷新 token 已失效。 */
    @Override
    public void revoke(String token) {
        RefreshTokenRecord record = tokens.get(token);
        if (record != null) {
            tokens.put(token, record.revoked());
        }
    }
}
