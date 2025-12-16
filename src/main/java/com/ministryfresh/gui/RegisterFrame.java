package com.ministryfresh.gui;

import com.ministryfresh.models.User;
import com.ministryfresh.repositories.UserRepository;
import com.ministryfresh.repositories.VacancyRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    private UserRepository userRepository;
    private VacancyRepository vacancyRepository;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame(UserRepository userRepository, VacancyRepository vacancyRepository) {
        this.userRepository = userRepository;
        this.vacancyRepository = vacancyRepository; // Инициализируем
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Регистрация - Министерство");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Регистрация в системе", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        formPanel.add(new JLabel("ФИО:"));
        fullNameField = new JTextField();
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Имя пользователя:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Подтвердите пароль:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);

        formPanel.add(new JLabel("Роль:"));
        String[] roles = {"Выберите роль", "Компания", "Центр занятости", "Гражданин"};
        roleComboBox = new JComboBox<>(roles);
        formPanel.add(roleComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        registerButton = new JButton("Зарегистрироваться");
        registerButton.addActionListener(new RegisterButtonListener());

        backButton = new JButton("Назад");
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String selectedRole = (String) roleComboBox.getSelectedItem();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "Все поля должны быть заполнены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "Пароли не совпадают!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 4) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "Пароль должен содержать минимум 4 символа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Выберите роль".equals(selectedRole)) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "Выберите роль!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String roleConstant = convertRoleToConstant(selectedRole);

            try {
                if (userRepository.isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Имя пользователя уже занято!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userRepository.isEmailExists(email)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Email уже зарегистрирован!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newUser = new User(username, email, password, fullName, roleConstant);
                boolean success = userRepository.registerUser(newUser);

                if (success) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Регистрация прошла успешно!\nРоль: " + selectedRole +
                                    "\nТеперь вы можете войти в систему.",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Ошибка при регистрации!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "Ошибка базы данных: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                System.err.println("Ошибка при регистрации пользователя: " + ex.getMessage());
            }
        }

        private String convertRoleToConstant(String roleDisplay) {
            switch (roleDisplay) {
                case "Компания": return User.ROLE_COMPANY;
                case "Центр занятости": return User.ROLE_EMPLOYMENT_CENTER;
                case "Гражданин": return User.ROLE_CITIZEN;
                default: return User.ROLE_CITIZEN;
            }
        }
    }
}