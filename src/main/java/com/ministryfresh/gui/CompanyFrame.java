package com.ministryfresh.gui;

import com.ministryfresh.models.User;
import com.ministryfresh.models.Vacancy;
import com.ministryfresh.repositories.VacancyRepository;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class CompanyFrame extends JFrame {
    private User currentUser;
    private VacancyRepository vacancyRepository;

    // Обновляем конструктор
    public CompanyFrame(User user, VacancyRepository vacancyRepository) {
        this.currentUser = user;
        this.vacancyRepository = vacancyRepository;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Увеличим размер
        setLocationRelativeTo(null); // Центрирование относительно экрана

        setMinimumSize(new Dimension(700, 500));

        setResizable(true);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Заголовок - сделаем его более заметным
        JLabel titleLabel = new JLabel("Панель компании - Министерство труда", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 70, 130));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Основной контент в центре
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Левая панель - информация о компании
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Информация о компании",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Добавим больше информации
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLabel = new JLabel("Название: " + currentUser.getFullName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(nameLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel contactLabel = new JLabel("Контактное лицо: " + currentUser.getUsername());
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(contactLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel emailLabel = new JLabel("Email: " + currentUser.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(emailLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel roleLabel = new JLabel("Роль: Компания");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(Color.BLUE);
        infoPanel.add(roleLabel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Правая панель - функции
        JPanel functionsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        functionsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Управление вакансиями",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Стилизуем кнопки
        JButton addVacancyButton = createStyledButton("➕ Добавить вакансию");
        addVacancyButton.addActionListener(e -> {
            new VacancyFormFrame(vacancyRepository, currentUser).setVisible(true);
        });

        JButton viewVacanciesButton = createStyledButton("📋 Мои вакансии");
        viewVacanciesButton.addActionListener(e -> showCompanyVacancies());

        JButton searchCandidatesButton = createStyledButton("👥 Поиск кандидатов");
        searchCandidatesButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton statisticsButton = createStyledButton("📊 Статистика");
        statisticsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton profileButton = createStyledButton("👤 Профиль");
        profileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton helpButton = createStyledButton("❓ Помощь");
        helpButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Система управления вакансиями\n\n" +
                            "1. Добавить вакансию - создание новой вакансии\n" +
                            "2. Мои вакансии - просмотр и управление вашими вакансиями\n" +
                            "3. Поиск кандидатов - поиск подходящих кандидатов\n" +
                            "4. Статистика - аналитика по вакансиям",
                    "Справка", JOptionPane.INFORMATION_MESSAGE);
        });

        functionsPanel.add(addVacancyButton);
        functionsPanel.add(viewVacanciesButton);
        functionsPanel.add(searchCandidatesButton);
        functionsPanel.add(statisticsButton);
        functionsPanel.add(profileButton);
        functionsPanel.add(helpButton);

        // Создаем панель с разделением
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, infoPanel, functionsPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);

        centerPanel.add(splitPane, BorderLayout.CENTER);

        // Нижняя панель - кнопка выхода
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = createLogoutButton("🚪 Выйти");
        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите выйти из системы?", "Подтверждение выхода",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                dispose();
                // Здесь нужно вернуться к окну логина
                // new LoginFrame(...).setVisible(true);
            }
        });

        bottomPanel.add(logoutButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Показать приветственное сообщение
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Добро пожаловать, " + currentUser.getFullName() + "!\n\n" +
                            "Вы вошли в систему как компания.\n" +
                            "Теперь вы можете создавать и управлять вакансиями.",
                    "Добро пожаловать", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Метод для создания стилизованных кнопок
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

    // Метод для кнопки выхода
    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(220, 80, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
    private void loadCompanyVacancies() {
        // Этот метод будет вызываться при необходимости
    }

    // Добавляем новый метод для отображения вакансий компании
    private void showCompanyVacancies() {
        try {
            List<Vacancy> vacancies = vacancyRepository.getCompanyVacancies(currentUser.getId());

            if (vacancies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "У вас пока нет вакансий", "Информация", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Создаем диалоговое окно со списком вакансий
            JDialog vacanciesDialog = new JDialog(this, "Мои вакансии", true);
            vacanciesDialog.setSize(600, 400);
            vacanciesDialog.setLocationRelativeTo(this);

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Vacancy v : vacancies) {
                String status = v.isActive() ? "Активна" : "Не активна";
                String salary = v.getSalary() != null ? v.getSalary().toString() + " руб." : "Не указана";
                listModel.addElement(String.format("%s | %s | %s | %s",
                        v.getTitle(), v.getLocation(), salary, status));
            }

            JList<String> vacanciesList = new JList<>(listModel);
            vacanciesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JButton editButton = new JButton("Редактировать");
            JButton deleteButton = new JButton("Удалить");
            JButton closeButton = new JButton("Закрыть");

            editButton.addActionListener(e -> {
                int selectedIndex = vacanciesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Vacancy selectedVacancy = vacancies.get(selectedIndex);
                    new VacancyFormFrame(vacancyRepository, currentUser, selectedVacancy).setVisible(true);
                    vacanciesDialog.dispose();
                }
            });

            deleteButton.addActionListener(e -> {
                int selectedIndex = vacanciesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    // ИСПРАВЛЕННАЯ СТРОКА
                    int confirm = JOptionPane.showConfirmDialog(vacanciesDialog,
                            "Удалить выбранную вакансию?", "Подтверждение", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Vacancy selectedVacancy = vacancies.get(selectedIndex);
                            boolean success = vacancyRepository.deleteVacancy(selectedVacancy.getId(), currentUser.getId());

                            if (success) {
                                // ИСПРАВЛЕННАЯ СТРОКА
                                JOptionPane.showMessageDialog(vacanciesDialog, "Вакансия удалена", "Успех", JOptionPane.INFORMATION_MESSAGE);
                                vacancies.remove(selectedIndex);
                                listModel.remove(selectedIndex);
                            }
                        } catch (Exception ex) {
                            // ИСПРАВЛЕННАЯ СТРОКА
                            JOptionPane.showMessageDialog(vacanciesDialog, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            closeButton.addActionListener(e -> vacanciesDialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(closeButton);

            vacanciesDialog.add(new JScrollPane(vacanciesList), BorderLayout.CENTER);
            vacanciesDialog.add(buttonPanel, BorderLayout.SOUTH);

            vacanciesDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
