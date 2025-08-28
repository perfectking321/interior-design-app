package com.interiordesign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

/**
 * Minimal DataSource configuration, using H2 in-memory DB.
 * Spring Boot will auto-configure DataSource from application.properties;
 * explicit bean provided to make JdbcTemplate available for injection.
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // DataSource is auto-configured by Spring Boot based on application.properties
}
