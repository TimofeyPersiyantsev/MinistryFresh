package com.ministryfresh;

import com.ministryfresh.config.DatabaseConfig;
import com.ministryfresh.database.PostgreSQLConnector;
import com.ministryfresh.gui.LoginFrame;
import com.ministryfresh.repositories.UserRepository;
import com.ministryfresh.repositories.VacancyRepository;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Устанавливаем Look and Feel для красивого интерфейса
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Игнорируем ошибку, если не удалось установить Look and Feel
            System.err.println("Не удалось установить системный Look and Feel: " + e.getMessage());
        }

        // Загружаем драйвер БД
        DatabaseConfig.loadDriver();

        // Создаем подключение к БД
        PostgreSQLConnector db = new PostgreSQLConnector(
                "ministry",
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
        );

        // Создаем репозитории
        UserRepository userRepository = new UserRepository(db);
        VacancyRepository vacancyRepository = new VacancyRepository(db); // Создаем vacancyRepository

        // Создаем таблицы
        try {
            userRepository.createUsersTable(); // или createUsersTable()
            vacancyRepository.createVacanciesTable(); // Создаем таблицу вакансий
            System.out.println("Таблицы успешно созданы/проверены");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при создании таблиц: " + e.getMessage(),
                    "Ошибка базы данных", JOptionPane.ERROR_MESSAGE);
            System.err.println("Ошибка базы данных: " + e.getMessage());
            return;
        }

        // Запускаем GUI в EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // Передаем оба репозитория в LoginFrame
            LoginFrame loginFrame = new LoginFrame(userRepository, vacancyRepository);
            loginFrame.setVisible(true);
        });
    }
}