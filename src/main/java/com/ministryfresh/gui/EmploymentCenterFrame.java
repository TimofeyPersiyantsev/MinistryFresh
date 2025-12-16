package com.ministryfresh.gui;

import com.ministryfresh.models.User;
import com.ministryfresh.models.CitizenEmployment;
import com.ministryfresh.models.JobDirection;
import com.ministryfresh.repositories.VacancyRepository;
import com.ministryfresh.repositories.CitizenEmploymentRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmploymentCenterFrame extends JFrame {
    private User currentUser;
    private VacancyRepository vacancyRepository;
    private CitizenEmploymentRepository employmentRepository;

    public EmploymentCenterFrame(User user, VacancyRepository vacancyRepository,
                                 CitizenEmploymentRepository employmentRepository) {
        this.currentUser = user;
        this.vacancyRepository = vacancyRepository;
        this.employmentRepository = employmentRepository;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Панель центра занятости - Министерство");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Панель центра занятости", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Информация о центре"));

        infoPanel.add(new JLabel("Центр: " + currentUser.getFullName()));
        infoPanel.add(new JLabel("Ответственный: " + currentUser.getUsername()));
        infoPanel.add(new JLabel("Email: " + currentUser.getEmail()));
        infoPanel.add(new JLabel("Роль: Центр занятости"));

        JPanel functionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        functionsPanel.setBorder(BorderFactory.createTitledBorder("Функции центра"));

        JButton manageCitizensButton = createStyledButton("👥 Управление гражданами");
        manageCitizensButton.addActionListener(e -> showCitizensManagement());

        JButton createDirectionButton = createStyledButton("📝 Направить на работу");
        createDirectionButton.addActionListener(e -> showVacancySelectionForNewDirection());

        JButton analyticsButton = createStyledButton("📊 Аналитика");
        analyticsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton reportsButton = createStyledButton("📋 Отчеты");
        reportsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Функция в разработке", "Информация", JOptionPane.INFORMATION_MESSAGE);
        });

        functionsPanel.add(manageCitizensButton);
        functionsPanel.add(createDirectionButton);
        functionsPanel.add(analyticsButton);
        functionsPanel.add(reportsButton);

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

    private void showCitizensManagement() {
        try {
            List<CitizenEmployment> citizens = employmentRepository.getRegisteredCitizens();

            JDialog dialog = new JDialog(this, "Зарегистрированные граждане", true);
            dialog.setSize(700, 400);
            dialog.setLocationRelativeTo(this);

            String[] columnNames = {"ID гражданина", "Дата регистрации", "Попыток", "Выплаты", "Статус"};
            Object[][] data = new Object[citizens.size()][5];

            for (int i = 0; i < citizens.size(); i++) {
                CitizenEmployment emp = citizens.get(i);
                data[i][0] = emp.getCitizenId();
                data[i][1] = emp.getRegistrationDate().toString();
                data[i][2] = emp.getAttemptsLeft();
                data[i][3] = emp.isReceivesBenefits() ? "Да" : "Нет";
                data[i][4] = emp.getStatusDisplay();
            }

            JTable citizensTable = new JTable(data, columnNames);

            JButton directionButton = new JButton("Направить на вакансию");
            directionButton.addActionListener(e -> {
                int selectedRow = citizensTable.getSelectedRow();
                if (selectedRow != -1) {
                    CitizenEmployment selected = citizens.get(selectedRow);
                    showVacancySelection(selected.getCitizenId());
                    dialog.dispose();
                }
            });

            dialog.add(new JScrollPane(citizensTable), BorderLayout.CENTER);
            dialog.add(directionButton, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showVacancySelectionForNewDirection() {
        JOptionPane.showMessageDialog(this,
                "Сначала выберите гражданина через 'Управление гражданами'",
                "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showVacancySelection(int citizenId) {
        try {
            List<com.ministryfresh.models.Vacancy> vacancies = vacancyRepository.getAllActiveVacancies();

            if (vacancies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Нет доступных вакансий", "Информация", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog(this, "Выбор вакансии для направления", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);

            String[] columnNames = {"ID", "Название", "Город", "Зарплата", "Тип"};
            Object[][] data = new Object[vacancies.size()][5];

            for (int i = 0; i < vacancies.size(); i++) {
                com.ministryfresh.models.Vacancy v = vacancies.get(i);
                data[i][0] = v.getId();
                data[i][1] = v.getTitle();
                data[i][2] = v.getLocation();
                data[i][3] = v.getSalary() != null ? v.getSalary() + " руб." : "Не указана";
                data[i][4] = v.getEmploymentTypeDisplay();
            }

            JTable vacanciesTable = new JTable(data, columnNames);

            JButton selectButton = new JButton("Направить гражданина");
            selectButton.addActionListener(e -> {
                int selectedRow = vacanciesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int vacancyId = (int) vacanciesTable.getValueAt(selectedRow, 0);
                    createJobDirection(citizenId, vacancyId);
                    dialog.dispose();
                }
            });

            JButton cancelButton = new JButton("Отмена");
            cancelButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(selectButton);
            buttonPanel.add(cancelButton);

            dialog.add(new JScrollPane(vacanciesTable), BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createJobDirection(int citizenId, int vacancyId) {
        try {
            JobDirection direction = new JobDirection(citizenId, vacancyId, currentUser.getId());
            int directionId = employmentRepository.createJobDirection(direction);

            CitizenEmployment employment = employmentRepository.getCitizenEmployment(citizenId);
            if (employment != null) {
            }

            JOptionPane.showMessageDialog(this,
                    "Направление успешно создано!\n" +
                            "ID направления: " + directionId,
                    "Успех", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }
}