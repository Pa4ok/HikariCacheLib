package ru.pa4ok.hikaricachelib;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class HikariCache {

    private static final ConcurrentHashMap<String, DataSourceCache> CACHE = new ConcurrentHashMap<>();

    public static Connection getConnection(String database) throws SQLException {
        DataSourceCache dataSourceCache = CACHE.get(database);
        if(dataSourceCache == null) {
            throw new IllegalArgumentException(String.format("Unknown database %s", database));
        }
        return dataSourceCache.getDataSource().getConnection();
    }

    public static synchronized boolean registerDatabase(String database, HikariConfig config) {
        if(!CACHE.containsKey(database)) {
            HikariDataSource dataSource = new HikariDataSource(config);
            CACHE.put(database, new DataSourceCache(config, dataSource));
            return true;
        }
        return false;
    }

    public static synchronized boolean unregisterDatabase(String database) {
        DataSourceCache dataSourceCache = CACHE.remove(database);
        if(dataSourceCache == null) {
            return false;
        }
        dataSourceCache.getDataSource().close();
        return true;
    }

    public static synchronized void tweakDatabase(String database, HikariConfig config) {
        unregisterDatabase(database);
        registerDatabase(database, config);
    }
}
