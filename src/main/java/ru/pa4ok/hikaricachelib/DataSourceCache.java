package ru.pa4ok.hikaricachelib;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class DataSourceCache {
    private final HikariConfig config;
    private final HikariDataSource dataSource;
}
