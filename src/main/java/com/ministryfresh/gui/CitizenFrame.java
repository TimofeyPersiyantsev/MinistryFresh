package com.ministryfresh.gui;

import java.util.List;
import com.ministryfresh.models.User;
import com.ministryfresh.repositories.VacancyRepository;

import com.ministryfresh.models.Vacancy;
import javax.swing.*;
import java.awt.*;

public class CitizenFrame extends JFrame {
    private User currentUser;

    private VacancyRepository vacancyRepository;

    public CitizenFrame(User user, VacancyRepository vacancyRepository) {
        this.currentUser = user;
        this.vacancyRepository = vacancyRepository;
        initializeUI();
    }

    // Метод для создания стилизованных кнопок (ДОБАВЬТЕ ЭТОТ МЕТОД)
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Эффект при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }

    // Метод для кнопки выхода (ДОБАВЬТЕ ЭТОТ МЕТОД)
    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(220, 80, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }


    private void initializeUI() {
        setTitle("Панель гражданина - Министерство труда");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 500));
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Заголовок
        JLabel titleLabel = new JLabel("Панель соискателя - Поиск работы", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 100, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Информация о пользователе
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Личная информация"));

        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLabel = new JLabel("ФИО: " + currentUser.getFullName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(nameLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel loginLabel = new JLabel("Логин: " + currentUser.getUsername());
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(loginLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel emailLabel = new JLabel("Email: " + currentUser.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(emailLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel roleLabel = new JLabel("Статус: Соискатель");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        roleLabel.setForeground(Color.GREEN.darker());
        infoPanel.add(roleLabel);

        infoPanel.add(Box.createVerticalGlue());

        // Функционал для гражданина - ИСПРАВЛЕНО
        JPanel functionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        functionsPanel.setBorder(BorderFactory.createTitledBorder("Поиск работы и карьера"));

        JButton searchVacanciesButton = createStyledButton("Поиск вакансий");
        searchVacanciesButton.addActionListener(e -> showAllVacancies());

        JButton myResumesButton = createStyledButton("Мои резюме");
        myResumesButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton myApplicationsButton = createStyledButton("Мои заявки");
        myApplicationsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton careerConsultationButton = createStyledButton("Карьерная консультация");
        careerConsultationButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        functionsPanel.add(searchVacanciesButton);
        functionsPanel.add(myResumesButton);
        functionsPanel.add(myApplicationsButton);
        functionsPanel.add(careerConsultationButton);

        // Дополнительные функции
        JPanel extraPanel = new JPanel(new GridLayout(1, 2, 15, 15));

        JButton statisticsButton = createStyledButton("📈 Статистика рынка");
        statisticsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton trainingButton = createStyledButton("Обучение");
        trainingButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        extraPanel.add(statisticsButton);
        extraPanel.add(trainingButton);

        // Объединяем панели
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(infoPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(functionsPanel, BorderLayout.CENTER);
        rightPanel.add(extraPanel, BorderLayout.SOUTH);

        centerPanel.add(rightPanel, BorderLayout.CENTER);

        // Кнопка выхода
        JButton logoutButton = createLogoutButton("Выйти");
        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите выйти?", "Подтверждение выхода",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Приветственное сообщение
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Добро пожаловать, " + currentUser.getFullName() + "!\n\n" +
                            "Вы вошли в систему как соискатель.\n" +
                            "Используйте кнопку 'Поиск вакансий' для просмотра доступных вакансий.",
                    "Добро пожаловать", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void showAllVacancies() {
        try {
            List<Vacancy> vacancies = vacancyRepository.getAllActiveVacancies();

            if (vacancies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Нет доступных вакансий", "Информация", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Создаем диалоговое окно со списком вакансий
            JDialog vacanciesDialog = new JDialog(this, "Доступные вакансии", true);
            vacanciesDialog.setSize(700, 500);
            vacanciesDialog.setLocationRelativeTo(this);

            // Создаем таблицу для отображения вакансий
            String[] columnNames = {"Название", "Город", "Зарплата", "Тип", "Опыт"};
            Object[][] data = new Object[vacancies.size()][5];

            for (int i = 0; i < vacancies.size(); i++) {
                Vacancy v = vacancies.get(i);
                data[i][0] = v.getTitle();
                data[i][1] = v.getLocation();
                data[i][2] = v.getSalary() != null ? v.getSalary() + " руб." : "Не указана";
                data[i][3] = v.getEmploymentTypeDisplay();
                data[i][4] = v.getExperienceLevelDisplay();
            }

            JTable vacanciesTable = new JTable(data, columnNames);
            vacanciesTable.setFillsViewportHeight(true);
            vacanciesTable.setRowHeight(25);

            JButton viewDetailsButton = new JButton("Просмотреть детали");
            JButton respondButton = new JButton("Откликнуться");
            JButton closeButton = new JButton("Закрыть");

            viewDetailsButton.addActionListener(e -> {
                int selectedRow = vacanciesTable.getSelectedRow();
                if (selectedRow != -1) {
                    Vacancy selectedVacancy = vacancies.get(selectedRow);
                    showVacancyDetails(selectedVacancy);
                }
            });

            closeButton.addActionListener(e -> vacanciesDialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(viewDetailsButton);
            buttonPanel.add(respondButton);
            buttonPanel.add(closeButton);

            vacanciesDialog.add(new JScrollPane(vacanciesTable), BorderLayout.CENTER);
            vacanciesDialog.add(buttonPanel, BorderLayout.SOUTH);

            vacanciesDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Метод showVacancyDetails() (ДОБАВЬТЕ ЭТОТ МЕТОД)
    private void showVacancyDetails(Vacancy vacancy) {
        JDialog detailsDialog = new JDialog(this, "Детали вакансии", true);
        detailsDialog.setSize(500, 400);
        detailsDialog.setLocationRelativeTo(this);

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder details = new StringBuilder();
        details.append("══════════════════════════════════════════\n");
        details.append("            ДЕТАЛИ ВАКАНСИИ              \n");
        details.append("══════════════════════════════════════════\n\n");
        details.append("Название: ").append(vacancy.getTitle()).append("\n\n");
        details.append("Описание:\n").append(vacancy.getDescription()).append("\n\n");

        if (vacancy.getRequirements() != null && !vacancy.getRequirements().isEmpty()) {
            details.append("Требования:\n").append(vacancy.getRequirements()).append("\n\n");
        }

        if (vacancy.getResponsibilities() != null && !vacancy.getResponsibilities().isEmpty()) {
            details.append("Обязанности:\n").append(vacancy.getResponsibilities()).append("\n\n");
        }

        details.append("──────────────────────────────────────────\n");
        details.append("Дополнительная информация:\n");
        details.append("Город: ").append(vacancy.getLocation()).append("\n");
        details.append("Зарплата: ").append(vacancy.getSalary() != null ? vacancy.getSalary() + " руб." : "Не указана").append("\n");
        details.append("Тип занятости: ").append(vacancy.getEmploymentTypeDisplay()).append("\n");
        details.append("Уровень опыта: ").append(vacancy.getExperienceLevelDisplay()).append("\n");

        detailsArea.setText(details.toString());

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> detailsDialog.dispose());

        detailsDialog.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        detailsDialog.add(closeButton, BorderLayout.SOUTH);

        detailsDialog.setVisible(true);
    }
}
