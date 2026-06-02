package com.addressbook.auth;

import java.util.Optional;

/** 定义刷新 token 的保存、查询和失效操作。 */
public interface RefreshTokenStore {
    /** 保存刷新 token 记录。 */
    void save(RefreshTokenRecord record);

    /** 查询刷新 token 记录。 */
    Optional<RefreshTokenRecord> find(String token);

    /** 让刷新 token 失效。 */
    void revoke(String token);
}
