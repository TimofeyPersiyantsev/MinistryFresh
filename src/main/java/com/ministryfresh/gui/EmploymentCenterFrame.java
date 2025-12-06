package com.ministryfresh.gui;

import com.ministryfresh.models.User;

import javax.swing.*;
import java.awt.*;

public class EmploymentCenterFrame extends JFrame {
    private User currentUser;

    public EmploymentCenterFrame(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Панель центра занятости - Министерство");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Заголовок
        JLabel titleLabel = new JLabel("Панель центра занятости", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Информация
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Информация о центре"));

        infoPanel.add(new JLabel("Центр: " + currentUser.getFullName()));
        infoPanel.add(new JLabel("Ответственный: " + currentUser.getUsername()));
        infoPanel.add(new JLabel("Email: " + currentUser.getEmail()));
        infoPanel.add(new JLabel("Роль: Центр занятости"));

        // Функционал для центра занятости
        JPanel functionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        functionsPanel.setBorder(BorderFactory.createTitledBorder("Функции центра"));

        JButton viewCitizensButton = new JButton("База граждан");
        JButton viewCompaniesButton = new JButton("База компаний");
        JButton analyticsButton = new JButton("Аналитика");
        JButton reportsButton = new JButton("Отчеты");

        functionsPanel.add(viewCitizensButton);
        functionsPanel.add(viewCompaniesButton);
        functionsPanel.add(analyticsButton);
        functionsPanel.add(reportsButton);

        // Кнопка выхода
        JButton logoutButton = new JButton("Выйти");
        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите выйти?", "Подтверждение выхода",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(functionsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        add(mainPanel);
    }
}