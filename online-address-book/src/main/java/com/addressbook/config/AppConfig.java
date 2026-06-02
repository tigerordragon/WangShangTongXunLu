package com.addressbook.config;

import com.addressbook.repository.RefreshTokenStore;
import com.addressbook.service.auth.AuthTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/** 配置登录模块需要的基础 Bean。 */
@Configuration
public class AppConfig {
    /** 提供系统时钟。 */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    /** 创建双 token 服务。 */
    @Bean
    public AuthTokenService authTokenService(RefreshTokenStore refreshTokenStore, Clock clock) {
        return new AuthTokenService("online-address-book-secret", refreshTokenStore, clock);
    }
}
