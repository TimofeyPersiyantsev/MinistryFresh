package com.ministryfresh.gui;

import com.ministryfresh.models.User;
import com.ministryfresh.repositories.UserRepository;
import com.ministryfresh.repositories.VacancyRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private UserRepository userRepository;
    private VacancyRepository vacancyRepository;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame(UserRepository userRepository, VacancyRepository vacancyRepository) {
        this.userRepository = userRepository;
        this.vacancyRepository = vacancyRepository; // Инициализируем
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Авторизация - Система авторизации");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        // Основная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Заголовок
        JLabel titleLabel = new JLabel("Вход в систему", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Панель формы
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Имя пользователя:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout());

        loginButton = new JButton("Войти");
        loginButton.addActionListener(new LoginButtonListener());

        registerButton = new JButton("Регистрация");
        registerButton.addActionListener(e -> {
            new RegisterFrame(userRepository, vacancyRepository).setVisible(true); // Передаем vacancyRepository
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Обработка нажатия Enter
        getRootPane().setDefaultButton(loginButton);

        add(mainPanel);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Валидация
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Введите имя пользователя и пароль!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                User user = userRepository.loginUser(username, password);

                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Добро пожаловать, " + user.getFullName() + "!",
                            "Успешный вход", JOptionPane.INFORMATION_MESSAGE);

                    // Открываем окно в зависимости от роли, передаем vacancyRepository
                    switch (user.getRole()) {
                        case User.ROLE_COMPANY:
                            new CompanyFrame(user, vacancyRepository).setVisible(true);
                            break;
                        case User.ROLE_EMPLOYMENT_CENTER:
                            new EmploymentCenterFrame(user).setVisible(true);
                            break;
                        case User.ROLE_CITIZEN:
                            new CitizenFrame(user, vacancyRepository).setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(LoginFrame.this,
                                    "Неизвестная роль пользователя!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                            return;
                    }
                    dispose(); // Закрываем окно авторизации
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Неверное имя пользователя или пароль!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Ошибка базы данных: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                System.err.println("Ошибка при авторизации: " + ex.getMessage());
            }
        }
    }
}
