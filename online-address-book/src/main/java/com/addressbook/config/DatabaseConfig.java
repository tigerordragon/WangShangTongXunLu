package com.addressbook.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.sql.DataSource;

/** 配置 MySQL 数据源（供 MyBatis 使用）。 */
@Configuration
@PropertySource("classpath:datasource.properties")
public class DatabaseConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource(
            @Value("${datasource.driver}") String driverClassName,
            @Value("${datasource.url}") String url,
            @Value("${datasource.username}") String username,
            @Value("${datasource.password}") String password,
            @Value("${datasource.pool.maximumPoolSize:10}") int maximumPoolSize) {
        ensureDriverOnClasspath(driverClassName);
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setPoolName("online-address-book");
        return new HikariDataSource(config);
    }

    /** 启动时校验 MySQL 驱动是否在 WEB-INF/lib 中，避免 Hikari 报 Failed to get driver instance。 */
    private void ensureDriverOnClasspath(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "未找到 MySQL 驱动 " + driverClassName
                            + "。请用 mvn package 生成 WAR 部署，或确认 IDEA Artifact 的 WEB-INF/lib 包含 mysql-connector-j。",
                    ex);
        }
    }
}
