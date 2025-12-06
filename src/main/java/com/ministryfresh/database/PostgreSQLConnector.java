package com.ministryfresh.database;

import java.sql.*;
import java.util.Properties;

public class PostgreSQLConnector {
    private Connection connection;
    private final String url;
    private final Properties connectionProps;

    public PostgreSQLConnector(String databaseName, String user, String password) {
        this.url = "jdbc:postgresql://localhost:5432/" + databaseName;

        this.connectionProps = new Properties();
        connectionProps.setProperty("user", user);
        connectionProps.setProperty("password", password);
        connectionProps.setProperty("ssl", "false");
    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(url, connectionProps);
                System.out.println("Успешное подключение к PostgreSQL!");
            } catch (SQLException e) {
                System.err.println("Ошибка подключения к PostgreSQL: " + e.getMessage());
                throw e;
            }
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Отключение от PostgreSQL");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed() && connection.isValid(2);
    }


}
