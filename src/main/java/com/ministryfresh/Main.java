package com.ministryfresh;

import com.ministryfresh.config.DatabaseConfig;
import com.ministryfresh.database.PostgreSQLConnector;
import com.ministryfresh.gui.LoginFrame;
import com.ministryfresh.repositories.UserRepository;
import com.ministryfresh.repositories.VacancyRepository;
import com.ministryfresh.repositories.CitizenEmploymentRepository;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Не удалось установить системный Look and Feel: " + e.getMessage());
        }

        DatabaseConfig.loadDriver();

        PostgreSQLConnector db = new PostgreSQLConnector(
                "ministry",
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
        );

        UserRepository userRepository = new UserRepository(db);
        VacancyRepository vacancyRepository = new VacancyRepository(db);
        CitizenEmploymentRepository employmentRepository = new CitizenEmploymentRepository(db);

        try {
            userRepository.createUsersTable();
            vacancyRepository.createVacanciesTable();
            employmentRepository.createEmploymentTables();
            System.out.println("Таблицы успешно созданы/проверены");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при создании таблиц: " + e.getMessage(),
                    "Ошибка базы данных", JOptionPane.ERROR_MESSAGE);
            System.err.println("Ошибка базы данных: " + e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userRepository, vacancyRepository, employmentRepository);
            loginFrame.setVisible(true);
        });
    }
}