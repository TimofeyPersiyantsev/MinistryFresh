package com.ministryfresh.config;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/ministry";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234"; // замените на ваш пароль
    private static final String DRIVER = "org.postgresql.Driver";

    public static String getUrl() { return URL; }
    public static String getUser() { return USER; }
    public static String getPassword() { return PASSWORD; }

    public static String getUrlForDatabase(String databaseName) {
        return "jdbc:postgresql://localhost:5432/" + databaseName;
    }

    public static void loadDriver() {
        try {
            Class.forName(DRIVER);
            System.out.println("PostgreSQL драйвер загружен успешно");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить драйвер PostgreSQL", e);
        }
    }
}
